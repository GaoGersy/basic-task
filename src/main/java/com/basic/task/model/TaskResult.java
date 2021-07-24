package com.basic.task.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Gersy
 * @date 2020/05/21
 */
public class TaskResult<T> implements Serializable {
    private String jobKey;
    private String text;
    private int type;
    @JsonIgnore
    private T data;
    private Date dataTime=new Date();

    public TaskResult(String text, int type) {
        this.text = text;
        this.type = type;
    }

    public TaskResult(String text, int type, T data) {
        this.text = text;
        this.type = type;
        this.data = data;
    }

    public static <T> TaskResult<T> success(String msg, T data) {
        return new TaskResult<>(msg, ResultType.SUCCESS, data);
    }

    public static <T> TaskResult<T> success(TaskInfo taskInfo, String msg, T data) {
        StringBuilder sb = new StringBuilder()
                .append("[ ")
                .append(taskInfo.getGroup())
                .append("-")
                .append(taskInfo.getTaskName())
                .append(" ]")
                .append(" 执行成功：")
                .append(msg);
        return new TaskResult<>(sb.toString(), ResultType.SUCCESS, data);
    }

    public static <T> TaskResult<T> info(String msg, T data) {
        return new TaskResult<>(msg, ResultType.INFO, data);
    }

    public static <T> TaskResult<T> warn(String msg, T data) {
        return new TaskResult<>(msg, ResultType.WARN, data);
    }

    public static <T> TaskResult<T> error(String msg, T data) {
        return new TaskResult<>(msg, ResultType.ERROR, data);
    }

    public static <T> TaskResult<T> error(String msg) {
        return new TaskResult<>(msg, ResultType.ERROR);
    }

    public static <T> TaskResult<T> error(TaskInfo taskInfo, String msg) {
        StringBuilder sb = new StringBuilder()
                .append("[ ")
                .append(taskInfo.getGroup())
                .append("-")
                .append(taskInfo.getTaskName())
                .append(" ]")
                .append(" 执行失败：")
                .append(msg);
        return new TaskResult<>(sb.toString(), ResultType.ERROR);
    }

    public Date getDataTime() {
        return dataTime;
    }

    public void setDataTime(Date dataTime) {
        this.dataTime = dataTime;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public String getJobKey() {
        return jobKey;
    }

    public void setJobKey(String jobKey) {
        this.jobKey = jobKey;
    }

    @Override
    public String toString() {
        return "TaskResult{" +
                "jobKey='" + jobKey + '\'' +
                ", text='" + text + '\'' +
                ", type=" + type +
                ", data=" + data +
                '}';
    }

    public interface ResultType {
        int SUCCESS = 0;
        int INFO = 1;
        int WARN = 2;
        int ERROR = 3;
    }
}
