package com.basic.task.model;

import com.basic.task.quartz.job.DynamicJob;

import org.quartz.CronScheduleBuilder;
import org.quartz.DateBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author Gersy
 * @since 2019-01-08
 */
public class ScheduleJob implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String CLASS_NAME = "className";
    public static final String METHOD_NAME = "methodName";
    public static final String PARAM_MAP = "paramMap";
    public static final String TASK_NAME = "taskName";
    public static final String TASK_INFO = "taskInfo";

    private String key;
    /**
     * 任务名称
     */
    private String jobName;
    /**
     * 任务分组
     */
    private String jobGroup;
    private String alias;
    /**
     * 任务状态 是否启动任务(0、未启动 1、启动)
     */
    private String jobStatus;

    private Integer jobStatusCode;
    /**
     * cron表达式
     */
    private String cronExpression;
    /**
     * 描述
     */
    private String description;
    /**
     * 任务执行时调用哪个类的方法 包名+类名
     */
    private String beanClass;
    /**
     * 任务调用的方法名
     */
    private String methodName;
    private String taskName;
    private List<TaskInfo> taskInfos;

    private Map<String, ParamInfo> params;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Map<String, ParamInfo> getParams() {
        return params;
    }

    public void setParams(Map<String, ParamInfo> params) {
        this.params = params;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public Integer getJobStatusCode() {
        return jobStatusCode;
    }

    public void setJobStatusCode(Integer jobStatusCode) {
        this.jobStatusCode = jobStatusCode;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(String beanClass) {
        this.beanClass = beanClass;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<TaskInfo> getTaskInfos() {
        return taskInfos;
    }

    public void setTaskInfos(List<TaskInfo> taskInfos) {
        this.taskInfos = taskInfos;
    }

    public String getKey() {
        if (key == null) {
            key = createKey(jobGroup , jobName);
        }
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public static ScheduleJob toScheduleJob(JobDataMap jobDataMap) {
        ScheduleJob scheduleJob = new ScheduleJob();
        Class<? extends ScheduleJob> aClass = ScheduleJob.class;
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            Object o = jobDataMap.get(name);
            if (o != null) {
                field.setAccessible(true);
                try {
                    field.set(scheduleJob, o);
                } catch (Exception e) {
                    e.printStackTrace();
                    String methodName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
                    try {
                        Method method = aClass.getDeclaredMethod(methodName, field.getType());
                        method.invoke(scheduleJob, o);
                    } catch (NoSuchMethodException ex) {
                        ex.printStackTrace();
                    } catch (IllegalAccessException ex) {
                        ex.printStackTrace();
                    } catch (InvocationTargetException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        return scheduleJob;
    }

    public void job2Map(JobDataMap jobDataMap) {
        Class<? extends ScheduleJob> aClass = ScheduleJob.class;
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            boolean aStatic = Modifier.isStatic(field.getModifiers());
            if (aStatic) {
                continue;
            }
            String name = field.getName();
            try {
                Object value = field.get(this);
                if (value != null) {
                    jobDataMap.put(name, value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public JobDetail createJobDetail() {
        // 创建jobDetail实例，绑定Job实现类
        // 指明job的名称，所在组的名称，以及绑定job类

        // 把作业和触发器注册到任务调度中
        JobDetail jobDetail = JobBuilder.newJob(DynamicJob.class)
                .withIdentity(jobName, jobGroup)// 任务名称和组构成任务key
                .storeDurably()
                .withDescription(description)
                .requestRecovery()
                .build();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        jobDataMap.put("taskInfos", taskInfos);
        jobDataMap.put("alias", alias);
        return jobDetail;
    }

    public Trigger createTrigger() {
        // 定义调度触发规则
        // 使用cornTrigger规则
        return TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup)// 触发器key
                .startAt(DateBuilder.futureDate(1, DateBuilder.IntervalUnit.SECOND))
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .startNow()
                .build();
    }

    public JobKey createJobKey() {
        return new JobKey(jobName, jobGroup);
    }

    public TriggerKey createTriggerKey() {
        return TriggerKey.triggerKey(jobName, jobGroup);
    }

    public boolean needReSchedule(String cronExpression) {
        return !this.cronExpression.equals(cronExpression);
    }

    public static String createKey(String group,String name){
        return group + ":" + name;
    }

    @Override
    public String toString() {
        return "ScheduleJob{" +
                ", jobName=" + jobName +
                ", jobGroup=" + jobGroup +
                ", jobStatus=" + jobStatus +
                ", cronExpression=" + cronExpression +
                ", description=" + description +
                ", beanClass=" + beanClass +
                ", methodName=" + methodName +
                "}";
    }
}
