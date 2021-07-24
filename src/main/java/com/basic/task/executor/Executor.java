package com.basic.task.executor;

import com.basic.task.model.TaskResult;

/**
 * @author Gersy
 * 处理每个任务执行后的结果信息，可以控制台打印，也可以通过websocket发送到前端页面展示等等
 */
public interface Executor {

    /**
     * 任务执行后的结果，以及信息的颜色等信息
     *
     * @param taskResult 任务执行后的结果
     */
    void onNext(TaskResult<?> taskResult);

    /**
     * 任务执行出错后的结果，以及信息的颜色等信息
     *
     * @param taskResult 任务执行出错后的结果
     */
    void onError(TaskResult<?> taskResult);

    /**
     * 任务执行完成后执行
     *
     * @param jobName 调度任务的名称
     */
    void onComplete(String jobName);
}