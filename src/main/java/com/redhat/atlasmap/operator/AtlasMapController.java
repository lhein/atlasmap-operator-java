package com.redhat.atlasmap.operator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.javaoperatorsdk.operator.api.Context;
import io.javaoperatorsdk.operator.api.Controller;
import io.javaoperatorsdk.operator.api.DeleteControl;
import io.javaoperatorsdk.operator.api.ResourceController;
import io.javaoperatorsdk.operator.api.UpdateControl;

@Controller
public class AtlasMapController implements ResourceController<AtlasMap> {
    static final String USERNAME_FORMAT = "%s-user";
    static final String SECRET_FORMAT = "%s-secret";

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final KubernetesClient kubernetesClient;

    public AtlasMapController(KubernetesClient kubernetesClient) {
        this.kubernetesClient = kubernetesClient;
    }

    @Override
    public UpdateControl<AtlasMap> createOrUpdateResource(AtlasMap resource, Context<AtlasMap> context) {
        // TODO 

        return null;
    }

    @Override
    public DeleteControl deleteResource(AtlasMap atlasMap, Context<AtlasMap> context) {
        log.info("Execution deleteResource for: {}", atlasMap.getMetadata().getName());

        // TODO

        try {
            return DeleteControl.DEFAULT_DELETE;
        } catch (Exception e) {
            return DeleteControl.NO_FINALIZER_REMOVAL;
        }
    }

}
