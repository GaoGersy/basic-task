package com.basic.task.model;

import java.io.Serializable;

public class ParamInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String alias;
    private String value = "";
    private String type;

    public ParamInfo() {
    }

    public ParamInfo(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }

    public ParamInfo(String name, String alias, String value) {
        this.name = name;
        this.alias = alias;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ParamInfo{" +
                "name='" + name + '\'' +
                ", alias='" + alias + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}