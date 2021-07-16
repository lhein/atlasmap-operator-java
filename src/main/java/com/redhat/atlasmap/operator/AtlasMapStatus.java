package com.redhat.atlasmap.operator;

import com.fasterxml.jackson.annotation.JsonAlias;

public class AtlasMapStatus {
    // The URL where AtlasMap can be accessed
    @JsonAlias({ "URL" })
	private String url;

    // The container image that AtlasMap is using
    @JsonAlias({ "image" })    
	private String image;

	// The current phase that the AtlasMap resource is in
    @JsonAlias({ "phase" })    
	private String phase;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }
}
