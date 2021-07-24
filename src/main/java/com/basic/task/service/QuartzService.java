package com.basic.task.service;


import com.basic.task.model.ScheduleJob;

import org.quartz.JobDataMap;
import org.quartz.JobKey;

import java.util.List;

public interface QuartzService {
    JobDataMap addJob(ScheduleJob job);

    List<JobDataMap> getAllJob();

    List<ScheduleJob> getRunningJob();

    JobDataMap pauseJob(ScheduleJob scheduleJob);

    JobDataMap resumeJob(ScheduleJob scheduleJob);

    boolean deleteJob(ScheduleJob scheduleJob);

    JobDataMap runJobNow(ScheduleJob scheduleJob);

    JobDataMap updateJob(ScheduleJob scheduleJob);

    boolean updateJobCron(ScheduleJob scheduleJob);

    JobDataMap interruptJob(ScheduleJob job);

    boolean isJobRunning(JobKey jobKey);
}
