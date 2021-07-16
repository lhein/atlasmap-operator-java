package com.redhat.atlasmap.operator;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.ShortNames;
import io.fabric8.kubernetes.model.annotation.Version;

@Group(AtlasMap.GROUP)
@Version(AtlasMap.VERSION)
@ShortNames("amap")
public class AtlasMap extends CustomResource<AtlasMapSpec, AtlasMapStatus> implements Namespaced {
    public static final String VERSION = "v1alpha1";
    public static final String GROUP = "atlasmap.javaoperatorsdk";
}
