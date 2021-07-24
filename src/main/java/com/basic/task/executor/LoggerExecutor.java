package com.basic.task.executor;


import com.basic.task.model.TaskResult;
import com.basic.task.service.WebSocketService;

/**
 * @author Gersy
 */
public class LoggerExecutor implements Executor {

    WebSocketService mWebSocketService;
    private String jobKey;

    public LoggerExecutor(String jobKey, WebSocketService webSocketService) {
        this.jobKey = jobKey;
        mWebSocketService = webSocketService;
    }

    @Override
    public void onNext(TaskResult<?> taskResult) {
        taskResult.setJobKey(jobKey);
        mWebSocketService.task(taskResult);
    }

    @Override
    public void onError(TaskResult<?> taskResult) {
        taskResult.setJobKey(jobKey);
        mWebSocketService.task(taskResult);
    }

    @Override
    public void onComplete(String jobName) {
        if (jobName == null) {
            return;
        }
        TaskResult<Object> taskResult = TaskResult.info(jobName + "执行完成", null);
        taskResult.setJobKey(jobKey);
        mWebSocketService.task(taskResult);
    }
}
