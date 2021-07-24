package com.basic.task.constants;

public enum DataType {
    STRING("string"),
    NUMBER("number"),
    BOOLEAN("bool"),
    ARRAY("array"),
    OBJECT("object");
    private final String type;

    DataType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
