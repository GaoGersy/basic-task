package com.basic.task.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Gersy
 */
public class PropertyInfo<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private String taskName;
    private T data;
    private Map<String, Object> propertiesMap;

    public PropertyInfo(T data) {
        this.data = data;
    }

    public PropertyInfo(String taskName) {
        this.taskName = taskName;
    }

    public PropertyInfo(String taskName, T data) {
        this.taskName = taskName;
        this.data = data;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void addProperty(String key, Object property) {
        if (propertiesMap == null) {
            propertiesMap = new HashMap<>(1);
        }
        propertiesMap.put(key, property);
    }

    public Object getProperty(String key) {
        if (propertiesMap == null) {
            return null;
        }
        return propertiesMap.get(key);
    }

    @Override
    public String toString() {
        return "PropertyInfo{" +
                "taskName='" + taskName + '\'' +
                ", data=" + data +
                ", propertiesMap=" + propertiesMap +
                '}';
    }
}