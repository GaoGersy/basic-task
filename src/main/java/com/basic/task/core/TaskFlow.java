package com.basic.task.core;


import com.basic.task.exception.TaskInterruptException;
import com.basic.task.executor.Executor;
import com.basic.task.logger.TaskLoggerFactory;
import com.basic.task.model.PropertyInfo;
import com.basic.task.model.TaskResult;
import com.basic.task.task.Task;

import java.util.List;

public class TaskFlow {
    private List<Task<?>> mTasks;
    //忽略异常
    private boolean ignoreError = true;
    private String projectName;
    private PropertyInfo propertyInfo;
    private Executor executor;
    private static Thread currentThread;
    private boolean isEndTask;
    private String jobKey;

    private TaskFlow() {
    }

    private TaskFlow(String projectName) {
        this.projectName = projectName;
    }

    public TaskFlow(String projectName, PropertyInfo propertyInfo) {
        this.projectName = projectName;
        this.propertyInfo = propertyInfo;
    }

    public static TaskFlow create(String projectName) {
        return new TaskFlow(projectName);
    }

    public TaskFlow jobKey(String jobKey) {
        this.jobKey = jobKey;
        return this;
    }

    public TaskFlow tasks(List<Task<?>> tasks) {
        this.mTasks = tasks;
        return this;
    }

    public String getProjectName() {
        return projectName;
    }

    public TaskFlow ignoreError(boolean ignoreError) {
        this.ignoreError = ignoreError;
        return this;
    }

    public void start(Executor executor) {
        currentThread = Thread.currentThread();
        this.executor = executor;
        if (executor == null) {
            throw new RuntimeException("Executor 不能为空");
        }
        try {
            execute(mTasks, propertyInfo);
            executor.onComplete(projectName);
        } catch (Exception e) {
            executor.onError(TaskResult.error("执行出错，错误信息：" + e.getMessage()));
            e.printStackTrace();
        }
    }

    public void interrupt() {
        isEndTask = true;
        if (!currentThread.isInterrupted()) {
            try {
                currentThread.interrupt();
            } catch (SecurityException ignore) {
            }
        }
        TaskLoggerFactory.getTaskLogger().w(jobKey, projectName + "终止执行");
    }

    private void execute(List<Task<?>> tasks, PropertyInfo propertyInfo) throws Exception {
        if (isEndTask) {
            //interrupt调用后通过异常结束线程
            throw new TaskInterruptException();
        }
        for (int i = 0; i < tasks.size(); i++) {
            Task<?> task = tasks.get(i);
            task.setPropertyInfo(propertyInfo);
            if (task.isFlat()) {
                TaskResult<?> taskResult = null;
                try {
                    taskResult = task.execute();
                } catch (Exception e) {
                    executor.onError(TaskResult.error("执行出错，错误信息：" + e.getMessage()));
                    return;
                }
                executor.onNext(taskResult);
                List<?> datas = (List<?>) taskResult.getData();
                if (datas == null || datas.size() == 0) {
                    return;
                }
                List<Task<?>> pluginList = tasks.subList(i + 1, tasks.size());
                tasks = tasks.subList(0, i);
                for (Object data : datas) {
                    try {//一个任务出错了不应该影响后面的任务的执行
                        execute(pluginList, new PropertyInfo<>(data));
                    } catch (TaskInterruptException e) {
                        throw new TaskInterruptException();
                    } catch (Exception e) {
                        e.printStackTrace();
                        executor.onError(TaskResult.error("执行出错，错误信息：" + e.getMessage()));
                    }
                }
            } else {
                try {
                    task.setPropertyInfo(propertyInfo);
                    executor.onNext(task.execute());
                } catch (Exception e) {
                    //忽略异常继续执行下一步
                    if (ignoreError) {
                        executor.onError(TaskResult.error("执行出错，错误信息：" + e.getMessage()));
                    } else {
                        throw e;
                    }
                }
            }
        }
    }
}
