package com.basic.task.model;

import java.io.Serializable;

/**
 * @author Gersy
 * @date 2020/5/21
 */
public class FilterInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 用以区分是文件还是文件夹，跟FileType对应
     */
    private int fileType;
    /**
     * 过滤的条件，以逗号分隔
     */
    private String condition;

    public FilterInfo() {
    }

    public FilterInfo(int fileType, String condition) {
        this.fileType = fileType;
        this.condition = condition;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
