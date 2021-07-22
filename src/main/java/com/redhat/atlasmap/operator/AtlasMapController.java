package com.redhat.atlasmap.operator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.openshift.api.model.Route;
import io.fabric8.openshift.api.model.RouteBuilder;
import io.fabric8.openshift.api.model.RoutePortBuilder;
import io.fabric8.openshift.api.model.RouteSpec;
import io.fabric8.openshift.api.model.RouteSpecBuilder;
import io.fabric8.kubernetes.api.model.ContainerBuilder;
import io.fabric8.kubernetes.api.model.ContainerPortBuilder;
import io.fabric8.kubernetes.api.model.IntOrStringBuilder;
import io.fabric8.kubernetes.api.model.LabelSelectorBuilder;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.api.model.OwnerReferenceBuilder;
import io.fabric8.kubernetes.api.model.PodSpecBuilder;
import io.fabric8.kubernetes.api.model.PodTemplateSpecBuilder;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceBuilder;
import io.fabric8.kubernetes.api.model.ServicePort;
import io.fabric8.kubernetes.api.model.ServiceSpecBuilder;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpecBuilder;
import io.javaoperatorsdk.operator.api.Context;
import io.javaoperatorsdk.operator.api.Controller;
import io.javaoperatorsdk.operator.api.DeleteControl;
import io.javaoperatorsdk.operator.api.ResourceController;
import io.javaoperatorsdk.operator.api.UpdateControl;

@Controller(name = "atlasmap")
public class AtlasMapController implements ResourceController<AtlasMap> {
    static final String USERNAME_FORMAT = "%s-user";
    static final String SECRET_FORMAT = "%s-secret";

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    KubernetesClient client;

    @Override
    public UpdateControl<AtlasMap> createOrUpdateResource(AtlasMap atlasmap, Context<AtlasMap> context) {
        Service newService = createAtlasMapService(atlasmap);
        client.services().create(newService);

        Route route = createAtlasMapRoute(atlasmap);
        
        Deployment deployment = client.apps().deployments().inNamespace(atlasmap.getMetadata().getNamespace())
                .withName(atlasmap.getMetadata().getName()).get();

        if (deployment == null) {
            Deployment newDeployment = createAtlasMapDeployment(atlasmap);
            client.apps().deployments().create(newDeployment);
            return UpdateControl.noUpdate();
        }

        int currentReplicas = deployment.getSpec().getReplicas();
        int requiredReplicas = atlasmap.getSpec().getReplicas();
        if (currentReplicas != requiredReplicas) {
            deployment.getSpec().setReplicas(requiredReplicas);
            client.apps().deployments().createOrReplace(deployment);
            return UpdateControl.noUpdate();
        }

        if (atlasmap.getStatus() == null) {
            atlasmap.setStatus(new AtlasMapStatus());
            return UpdateControl.updateStatusSubResource(atlasmap);
        }

        return UpdateControl.noUpdate();
    }

    @Override
    public DeleteControl deleteResource(AtlasMap atlasMap, Context<AtlasMap> context) {
        log.info("Execution deleteResource for: {}", atlasMap.getMetadata().getName());

        try {

            // TODO for some reason the service is not deleted
            Service svc = client.services().inNamespace(atlasMap.getMetadata().getNamespace())
            .withName(atlasMap.getMetadata().getName()).get();

            if (svc != null) {
                client.services().delete(svc);
            }

            return DeleteControl.DEFAULT_DELETE;
        } catch (Exception e) {
            return DeleteControl.NO_FINALIZER_REMOVAL;
        }
    }

    private Route createAtlasMapRoute(AtlasMap atlasmap) {
        return new RouteBuilder()
            .withNewMetadata().withName("route").endMetadata()
            .withNewSpec().withHost("www.example.com").withNewTo().withKind("Service").withName(atlasmap.getMetadata().getName()).endTo().endSpec()
        .build();
    }

    private Service createAtlasMapService(AtlasMap atlasmap) {
        ServicePort sp = new ServicePort();
        sp.setPort(8585);
        sp.setTargetPort(new IntOrStringBuilder().withStrVal("http").build());
        sp.setProtocol("TCP");
        return new ServiceBuilder()
                .withMetadata(new ObjectMetaBuilder().withName(atlasmap.getMetadata().getName())
                    .withNamespace(atlasmap.getMetadata().getNamespace())
                    .withOwnerReferences(new OwnerReferenceBuilder().withApiVersion("v1alpha1").withKind("AtlasMap")
                                .withName(atlasmap.getMetadata().getName()).withUid(atlasmap.getMetadata().getUid())
                                .build())
                        .build())
                .withSpec(new ServiceSpecBuilder().withPorts(
                    sp
                ).build())
            .build();
    }

    private Deployment createAtlasMapDeployment(AtlasMap atlasmap) {
        return new DeploymentBuilder()
                .withMetadata(new ObjectMetaBuilder().withName(atlasmap.getMetadata().getName())
                        .withNamespace(atlasmap.getMetadata().getNamespace())
                        .withOwnerReferences(new OwnerReferenceBuilder().withApiVersion("v1alpha1").withKind("AtlasMap")
                                .withName(atlasmap.getMetadata().getName()).withUid(atlasmap.getMetadata().getUid())
                                .build())
                        .build())
                .withSpec(new DeploymentSpecBuilder().withReplicas(atlasmap.getSpec().getReplicas())
                        .withSelector(new LabelSelectorBuilder().withMatchLabels(labelsForAtlasMap(atlasmap)).build())
                        .withTemplate(new PodTemplateSpecBuilder()
                                .withMetadata(new ObjectMetaBuilder().withLabels(labelsForAtlasMap(atlasmap)).build())
                                .withSpec(new PodSpecBuilder().withContainers(new ContainerBuilder()
                                        .withImage("docker.io/atlasmap/atlasmap:1.43").withName("atlasmap")
                                        .withPorts(List.of(new ContainerPortBuilder().withContainerPort(8585).withName("http").build(), new ContainerPortBuilder().withContainerPort(8778).withName("jolokia").build(), new ContainerPortBuilder().withContainerPort(9779).withName("prometheus").build()))
                                        .build()).build())
                                .build())
                        .build())
                .build();
    }

    private Map<String, String> labelsForAtlasMap(AtlasMap atlasmap) {
        Map<String, String> labels = new HashMap<>();
        labels.put("app", "atlasmap");
        labels.put("atlasmap_cr", atlasmap.getMetadata().getName());
        return labels;
    }
}
