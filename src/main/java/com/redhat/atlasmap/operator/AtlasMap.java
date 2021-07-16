package com.redhat.atlasmap.operator;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Version;

@Group("atlasmap.javaoperatorsdk")
@Version("v1")
public class AtlasMap extends CustomResource<AtlasMapSpec, AtlasMapStatus> implements Namespaced {
}
