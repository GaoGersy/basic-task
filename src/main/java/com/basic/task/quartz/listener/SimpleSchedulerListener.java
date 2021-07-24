package com.basic.task.quartz.listener;

import com.basic.common.auto.ApplicationContextRegister;
import com.basic.common.utils.SuperLogger;
import com.basic.task.model.JobStatus;
import com.basic.task.service.WebSocketService;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.SchedulerListener;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

public class SimpleSchedulerListener implements SchedulerListener {

    private final WebSocketService webSocketService;

    public SimpleSchedulerListener() {
        webSocketService = ApplicationContextRegister.getBean(WebSocketService.class);
    }

    //用于部署JobDetail时调用
    @Override
    public void jobScheduled(Trigger trigger) {
        SuperLogger.e("SimpleSchedulerListener======>jobScheduled：" + trigger.toString());
    }

    //用于卸载JobDetail时调用
    @Override
    public void jobUnscheduled(TriggerKey triggerKey) {
        SuperLogger.e("SimpleSchedulerListener======>jobUnscheduled：" + triggerKey.toString());
    }

    //当一个Trigger来到了再也不会触发的状态时调用这个方法，除非这个Job已设置成了持久性，否则它就会从Scheduler中移除
    @Override
    public void triggerFinalized(Trigger trigger) {
        SuperLogger.e("SimpleSchedulerListener======>triggerFinalized：" + trigger.toString());
        webSocketService.jobStatus(JobStatus.ended(trigger));
    }

    //Scheduler调用这个方法是发生在一个Trigger或Trigger组被暂停时
    @Override
    public void triggerPaused(TriggerKey triggerKey) {
        SuperLogger.e("SimpleSchedulerListener======>triggerPaused：" + triggerKey.toString());
    }

    //假如是Trigger组的话，triggerName参数将为null
    @Override
    public void triggersPaused(String triggerGroup) {
        SuperLogger.e("SimpleSchedulerListener======>triggersPaused：" + triggerGroup.toString());
    }

    //Scheduler调用这个方法是发生在一个Trigger或Trigger组从暂停中恢复时
    @Override
    public void triggerResumed(TriggerKey triggerKey) {
        SuperLogger.e("SimpleSchedulerListener======>triggerResumed：" + triggerKey.toString());
    }

    //假如是Trigger组的话，triggerName参数将为null
    @Override
    public void triggersResumed(String triggerGroup) {
        SuperLogger.e("SimpleSchedulerListener======>triggersResumed：" + triggerGroup.toString());
    }

    //Scheduler添加一个jobDetail时触发
    @Override
    public void jobAdded(JobDetail jobDetail) {
        SuperLogger.e("SimpleSchedulerListener======>jobAdded：" + jobDetail.toString());
    }

    //Scheduler删除一个jobDetail时触发
    @Override
    public void jobDeleted(JobKey jobKey) {
        SuperLogger.e("SimpleSchedulerListener======>jobDeleted："+jobKey);
    }

    //Scheduler暂停一个jobDetail时触发
    @Override
    public void jobPaused(JobKey jobKey) {
        SuperLogger.e("SimpleSchedulerListener======>jobPaused："+jobKey);
    }

    //Scheduler暂停一个jobDetail组时触发
    @Override
    public void jobsPaused(String jobGroup) {
        SuperLogger.e("SimpleSchedulerListener======>jobsPaused："+jobGroup);
    }

    //Scheduler从暂停中恢复一个jobDetail时触发
    @Override
    public void jobResumed(JobKey jobKey) {
        SuperLogger.e("SimpleSchedulerListener======>jobResumed："+jobKey);
    }

    //Scheduler从暂停中恢复一个jobDetail组时触发
    @Override
    public void jobsResumed(String jobGroup) {
        SuperLogger.e("SimpleSchedulerListener======>jobsResumed()");
    }

    //在Scheduler的正常运行期间产生一个严重错误时调用这个方法
    @Override
    public void schedulerError(String msg, SchedulerException cause) {
        SuperLogger.e("SimpleSchedulerListener======>schedulerError："+msg);
    }

    //当Scheduler处于StandBy模式时，调用该方法
    @Override
    public void schedulerInStandbyMode() {
        SuperLogger.e("SimpleSchedulerListener======>schedulerError()");
    }

    //当Scheduler启动后调用该方法
    @Override
    public void schedulerStarted() {
        SuperLogger.e("SimpleSchedulerListener======>schedulerStarted()");
    }

    //当Scheduler启动时调用该方法
    @Override
    public void schedulerStarting() {
        SuperLogger.e("SimpleSchedulerListener======>schedulerStarting()");
    }

    //当Scheduler停止后，调用该方法
    @Override
    public void schedulerShutdown() {
        SuperLogger.e("SimpleSchedulerListener======>schedulerShutdown()");
    }

    //档Scheduler停止时，调用该方法
    @Override
    public void schedulerShuttingdown() {
        SuperLogger.e("SimpleSchedulerListener======>schedulerShuttingdown()");
    }

    //当Scheduler中的数据被清除时，调用该方法
    @Override
    public void schedulingDataCleared() {
        SuperLogger.e("SimpleSchedulerListener======>schedulingDataCleared()");
    }
}
