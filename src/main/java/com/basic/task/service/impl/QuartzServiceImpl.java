package com.basic.task.service.impl;


import com.basic.task.model.JobStatus;
import com.basic.task.model.ParamInfo;
import com.basic.task.model.ScheduleJob;
import com.basic.task.service.QuartzService;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @description: 计划任务管理
 */
@Service
public class QuartzServiceImpl implements QuartzService {

    @Autowired
    private Scheduler scheduler;
//    @Autowired
//    private CommonHandler commonHandler;

    /**
     * 添加任务
     * @return
     */
    @Override
    public JobDataMap addJob(ScheduleJob job) {
        try {
            scheduler.scheduleJob(job.createJobDetail(), job.createTrigger());
            return getJobDataMap(job.createJobKey());
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取所有计划中的任务列表
     */
    @Override
    public List<JobDataMap> getAllJob() {
        GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
        try {
            Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
            LinkedList<JobDataMap> jobList = new LinkedList<>();
            for (JobKey jobKey : jobKeys) {
                JobDataMap jobDataMap = getJobDataMap(jobKey);
                Integer jobStatusCode = (Integer) jobDataMap.get("jobStatusCode");
                if(jobStatusCode!=null&&Trigger.TriggerState.NORMAL.ordinal()==jobStatusCode) {
                    jobList.addFirst(jobDataMap);
                }else {
                    jobList.addLast(jobDataMap);
                }
            }
            return jobList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private JobDataMap getJobDataMap(JobKey jobKey) throws SchedulerException {
        List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        JobDataMap jobDataMap = (JobDataMap) jobDetail.getJobDataMap().clone();
        jobDataMap.put("key", ScheduleJob.createKey(jobKey.getGroup(), jobKey.getName()));
        jobDataMap.put("jobName", jobKey.getName());
        jobDataMap.put("jobGroup", jobKey.getGroup());
        jobDataMap.put("description", jobDetail.getDescription());
        int pendingCount = 0;//阻塞中的任务数量
        Date nextFireTime = null;
        for (Trigger trigger : triggers) {
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
            if (trigger instanceof CronTrigger) {
                CronTrigger cronTrigger = (CronTrigger) trigger;
                jobDataMap.put("cronExpression", cronTrigger.getCronExpression());
                jobDataMap.put("jobStatus", getStatusName(triggerState.ordinal()));
                jobDataMap.put("jobStatusCode", triggerState == Trigger.TriggerState.PAUSED ? triggerState.ordinal() : Trigger.TriggerState.NORMAL.ordinal());
                nextFireTime = trigger.getNextFireTime();
            }
            if (triggerState == Trigger.TriggerState.BLOCKED) {
                pendingCount++;
            }
        }
        if (pendingCount > 0) {
            jobDataMap.put("pendingCount", pendingCount);
        }
        jobDataMap.put("status", isJobRunning(jobKey) ? JobStatus.STARTED : JobStatus.ENDED);
        jobDataMap.put("nextFireTime", nextFireTime);
        return jobDataMap;
    }

    private String getStatusName(int code) {
        switch (code) {
            case 0:
                return "没有";
            case 1:
                return "正常";
            case 2:
                return "暂停";
            case 3:
                return "完成";
            case 4:
                return "出错";
            case 5:
                return "阻塞";
            default:
                return "未知";
        }
    }

    /**
     * 所有正在运行的job
     */
    @Override
    public List<ScheduleJob> getRunningJob() {
        try {
            List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
            List<ScheduleJob> jobList = new LinkedList<>();
            for (JobExecutionContext executingJob : executingJobs) {
                ScheduleJob job = new ScheduleJob();
                JobDetail jobDetail = executingJob.getJobDetail();
                JobKey jobKey = jobDetail.getKey();
                Trigger trigger = executingJob.getTrigger();
                job.setJobName(jobKey.getName());
                job.setJobGroup(jobKey.getGroup());
                job.setDescription("触发器:" + trigger.getKey());
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                job.setBeanClass(jobDetail.getJobDataMap().getString(ScheduleJob.CLASS_NAME));
                job.setJobStatus(triggerState.name());
                job.setParams((Map<String, ParamInfo>) jobDetail.getJobDataMap().get(ScheduleJob.PARAM_MAP));
                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    job.setCronExpression(cronExpression);
                }
                jobList.add(job);
            }
            return jobList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 暂停一个job
     */
    @Override
    public JobDataMap pauseJob(ScheduleJob scheduleJob) {
        try {
            JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
            scheduler.pauseJob(jobKey);
            return getJobDataMap(jobKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 恢复一个job
     */
    @Override
    public JobDataMap resumeJob(ScheduleJob scheduleJob) {
        try {
            JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
            scheduler.resumeJob(jobKey);
            return getJobDataMap(jobKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除一个job
     */
    @Override
    public boolean deleteJob(ScheduleJob scheduleJob) {
        try {
            JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
            return scheduler.deleteJob(jobKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 立即执行job
     */
    @Override
    public JobDataMap runJobNow(ScheduleJob scheduleJob) {
        try {
            JobKey jobKey = scheduleJob.createJobKey();
//            boolean jobRunning = isJobRunning(jobKey);
//            if (jobRunning){
//                throw new RuntimeException(ScheduleJob.createKey(jobKey.getGroup(),jobKey.getName())+"已经在运行，无需再次启动！");
//            }
            scheduler.triggerJob(jobKey);
            return getJobDataMap(jobKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public JobDataMap updateJob(ScheduleJob scheduleJob) {
        try {
            scheduler.addJob(scheduleJob.createJobDetail(), true);
            if (scheduleJob.needReSchedule(getCronExpression(scheduleJob))) {
                updateJobCron(scheduleJob);
            }
            return getJobDataMap(scheduleJob.createJobKey());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getCronExpression(ScheduleJob scheduleJob) {
        try {
            Trigger trigger = scheduler.getTrigger(scheduleJob.createTriggerKey());
            if (trigger instanceof CronTrigger) {
                CronTrigger cronTrigger = (CronTrigger) trigger;
                return cronTrigger.getCronExpression();
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 更新job时间表达式
     */
    @Override
    public boolean updateJobCron(ScheduleJob scheduleJob) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public JobDataMap interruptJob(ScheduleJob job) {
        try {
            scheduler.interrupt(new JobKey(job.getJobName(), job.getJobGroup()));
            return getJobDataMap(job.createJobKey());
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isJobRunning(JobKey jobKey) {
        try {
            List<JobExecutionContext> currentlyExecutingJobs = scheduler.getCurrentlyExecutingJobs();
            for (JobExecutionContext currentlyExecutingJob : currentlyExecutingJobs) {
                if (currentlyExecutingJob.getJobDetail().getKey().equals(jobKey)) {
                    return true;
                }
            }
            return false;
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return false;
    }
}