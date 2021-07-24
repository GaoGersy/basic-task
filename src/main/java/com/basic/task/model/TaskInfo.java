package com.basic.task.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 前端创建任务后会传递给后台的对象
 * @author Gersy
 * @date 2020/5/21
 */
public class TaskInfo implements Serializable {
    private static final long serialVersionUID = -1L;
    private boolean parserTask;
    private String jobKey;
    private String taskName;
    private String group;
    private String className;
    private String methodName;
    private List<FilterInfo> filterInfos;
    private Map<String, ParamInfo> paramInfoMap;

    public TaskInfo() {
    }

    public TaskInfo(String taskName, String group, String className, List<FilterInfo> filterInfos, Map<String, ParamInfo> paramInfoMap) {
        this.taskName = taskName;
        this.group = group;
        this.className = className;
        this.filterInfos = filterInfos;
        this.paramInfoMap = paramInfoMap;
    }

    public TaskInfo(boolean parserTask, String taskName,String group, String className, String methodName, List<FilterInfo> filterInfos,Map<String, ParamInfo> paramInfoMap) {
        this.parserTask = parserTask;
        this.taskName = taskName;
        this.group = group;
        this.className = className;
        this.methodName = methodName;
        this.filterInfos = filterInfos;
        this.paramInfoMap = paramInfoMap;
    }

    public TaskInfo(String taskName, String group,String className, Map<String, ParamInfo> paramInfoMap) {
        this.taskName = taskName;
        this.group = group;
        this.className = className;
        this.paramInfoMap = paramInfoMap;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Map<String, ParamInfo> getParamInfoMap() {
        return paramInfoMap;
    }

    public void setParamInfoMap(Map<String, ParamInfo> paramInfoMap) {
        this.paramInfoMap = paramInfoMap;
    }

    public String getJobKey() {
        return jobKey;
    }

    public void setJobKey(String jobKey) {
        this.jobKey = jobKey;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public boolean isParserTask() {
        return parserTask;
    }

    public void setParserTask(boolean parserTask) {
        this.parserTask = parserTask;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<FilterInfo> getFilterInfos() {
        return filterInfos;
    }

    public void setFilterInfos(List<FilterInfo> filterInfos) {
        this.filterInfos = filterInfos;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskInfo taskInfo = (TaskInfo) o;
        return parserTask == taskInfo.parserTask &&
                Objects.equals(taskName, taskInfo.taskName) &&
                Objects.equals(group, taskInfo.group) &&
                Objects.equals(className, taskInfo.className) &&
                Objects.equals(methodName, taskInfo.methodName) &&
                Objects.equals(filterInfos, taskInfo.filterInfos) &&
                Objects.equals(paramInfoMap, taskInfo.paramInfoMap);
    }

    @Override
    public String toString() {
        return "TaskInfo{" +
                "parserTask=" + parserTask +
                ", taskName='" + taskName + '\'' +
                ", group='" + group + '\'' +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", filterInfos=" + filterInfos +
                ", paramInfoMap=" + paramInfoMap +
                '}';
    }
}
