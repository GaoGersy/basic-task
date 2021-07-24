package com.basic.task.quartz.listener;

import com.basic.common.auto.ApplicationContextRegister;
import com.basic.common.utils.SuperLogger;
import com.basic.task.model.JobStatus;
import com.basic.task.service.WebSocketService;

import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerListener;

public class SimpleTriggerListener implements TriggerListener {
    private static final WebSocketService WEB_SOCKET_SERVICE = ApplicationContextRegister.getBean(WebSocketService.class);
//    private static final QuartzService QUARTZ_SERVICE = ApplicationContextRegister.getBean(QuartzService.class);

    @Override
    public String getName() {
        return "SimpleTriggerListener";
    }

    /**
     * (1)
     * Trigger被激发 它关联的job即将被运行
     * Called by the Scheduler when a Trigger has fired, and it's associated JobDetail is about to be executed.
     */
    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {
//        SuperLogger.e("triggerFired:"+trigger);
        WEB_SOCKET_SERVICE.jobStatus(JobStatus.started(trigger));
    }

    /**
     * (2)
     * Trigger被激发 它关联的job即将被运行,先执行(1)，在执行(2) 如果返回TRUE 那么任务job会被终止
     * Called by the Scheduler when a Trigger has fired, and it's associated JobDetail is about to be executed
     */
    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
//        SuperLogger.e("vetoJobExecution："+trigger);
//        JobKey jobKey = context.getTrigger().getJobKey();
//        if (QUARTZ_SERVICE.isJobRunning(jobKey)) {
//            SuperLogger.e(ScheduleJob.createKey(jobKey.getGroup(), jobKey.getName()) + "正在运行，跳过本次执行");
//            return true;
//        }
        return false;
    }

    /**
     * (3) 当Trigger错过被激发时执行,比如当前时间有很多触发器都需要执行，但是线程池中的有效线程都在工作，
     * 那么有的触发器就有可能超时，错过这一轮的触发。
     * Called by the Scheduler when a Trigger has misfired.
     */
    @Override
    public void triggerMisfired(Trigger trigger) {
        SuperLogger.e("triggerMisfired：" + trigger);
    }

    /**
     * (4) 任务完成时触发
     * Called by the Scheduler when a Trigger has fired, it's associated JobDetail has been executed
     * and it's triggered(xx) method has been called.
     */
    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext context, Trigger.CompletedExecutionInstruction triggerInstructionCode) {
//        SuperLogger.e("triggerComplete:"+trigger);
        WEB_SOCKET_SERVICE.jobStatus(JobStatus.ended(trigger));
    }
}
