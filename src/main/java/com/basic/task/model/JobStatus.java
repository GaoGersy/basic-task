package com.basic.task.model;

import org.quartz.JobKey;
import org.quartz.Trigger;

import java.util.Date;

public class JobStatus {
    public static final Integer STARTED = 1;
    public static final Integer ENDED = 0;
    private String key;
    /**
     * 任务状态 是否启动任务(0、任务结束 1、任务开始)
     */
    private Integer status;

    private Date nextFireTime;

    public JobStatus() {
    }

    public JobStatus(String key, Integer status) {
        this.key = key;
        this.status = status;
    }

    public JobStatus(String key, Integer status, Date nextFireTime) {
        this.key = key;
        this.status = status;
        this.nextFireTime = nextFireTime;
    }

    public static JobStatus started(Trigger trigger) {
        JobKey jobKey = trigger.getJobKey();
        return new JobStatus(ScheduleJob.createKey(jobKey.getGroup(), jobKey.getName()), STARTED, trigger.getNextFireTime());
    }

    public static JobStatus ended(Trigger trigger) {
        JobKey jobKey = trigger.getJobKey();
        return new JobStatus(ScheduleJob.createKey(jobKey.getGroup(), jobKey.getName()), ENDED, trigger.getNextFireTime());
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getNextFireTime() {
        return nextFireTime;
    }

    public void setNextFireTime(Date nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    @Override
    public String toString() {
        return "JobStatus{" +
                "key='" + key + '\'' +
                ", status=" + status +
                ", nextFireTime=" + nextFireTime +
                '}';
    }
}
