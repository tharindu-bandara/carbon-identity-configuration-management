package org.wso2.carbon.identity.configuration.mgt.core.model;

import java.util.List;

public class ResourceAdd {

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public List<Attribute> getAttributes() {

        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {

        this.attributes = attributes;
    }

    public ResourceFile getFile() {

        return file;
    }

    public void setFile(ResourceFile file) {

        this.file = file;
    }

    private String name;
    private List<Attribute> attributes;
    private ResourceFile file;
}
