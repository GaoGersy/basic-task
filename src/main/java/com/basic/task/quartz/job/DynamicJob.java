package com.basic.task.quartz.job;


import com.basic.common.auto.ApplicationContextRegister;
import com.basic.task.core.CommonHandler;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.PersistJobDataAfterExecution;
import org.quartz.Trigger;
import org.quartz.UnableToInterruptJobException;

import java.lang.reflect.InvocationTargetException;

//不允许并发执行
@DisallowConcurrentExecution
//持久化
@PersistJobDataAfterExecution
public class DynamicJob implements InterruptableJob {

    private static final CommonHandler HANDLER = ApplicationContextRegister.getBean(CommonHandler.class);

    private JobKey jobKey;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        Trigger trigger = jobExecutionContext.getTrigger();
        jobKey = trigger.getJobKey();
        try {
            HANDLER.start(jobExecutionContext.getJobDetail());
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        try {
            HANDLER.interrupt(jobKey);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
