package com.redhat.atlasmap.operator;

import com.fasterxml.jackson.annotation.JsonAlias;

public class AtlasMapSpec {
    // Replicas determines the desired number of running AtlasMap pods
    @JsonAlias({ "replicas" })    
    private Integer replicas;

    // RouteHostName sets the host name to use on the Ingress or OpenShift Route
    @JsonAlias({ "routeHostName" })    
    private String routeHostName;
    
    // Version sets the version of the container image used for AtlasMap
    @JsonAlias({ "version" })    
    private String version;
    
    // The amount of CPU to request
    // +kubebuilder:validation:Pattern=[0-9]+m?$
    @JsonAlias({ "requestCPU" })    
    private String requestCPU;
    
    // The amount of memory to request
    // +kubebuilder:validation:Pattern=[0-9]+([kKmMgGtTpPeE]i?)?$
    @JsonAlias({ "requestMemory" })    
    private String requestMemory;
    
    // The amount of CPU to limit
    // +kubebuilder:validation:Pattern=[0-9]+m?$
    @JsonAlias({ "limitCPU" })    
    private String limitCPU;
    
    // The amount of memory to request
    // +kubebuilder:validation:Pattern=[0-9]+([kKmMgGtTpPeE]i?)?$
    @JsonAlias({ "limitMemory" })    
    private String limitMemory;

    public Integer getReplicas() {
        return replicas;
    }

    public void setReplicas(Integer replicas) {
        this.replicas = replicas;
    }

    public String getRouteHostName() {
        return routeHostName;
    }

    public void setRouteHostName(String routeHostName) {
        this.routeHostName = routeHostName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRequestCPU() {
        return requestCPU;
    }

    public void setRequestCPU(String requestCPU) {
        this.requestCPU = requestCPU;
    }

    public String getRequestMemory() {
        return requestMemory;
    }

    public void setRequestMemory(String requestMemory) {
        this.requestMemory = requestMemory;
    }

    public String getLimitCPU() {
        return limitCPU;
    }

    public void setLimitCPU(String limitCPU) {
        this.limitCPU = limitCPU;
    }

    public String getLimitMemory() {
        return limitMemory;
    }

    public void setLimitMemory(String limitMemory) {
        this.limitMemory = limitMemory;
    }
}
