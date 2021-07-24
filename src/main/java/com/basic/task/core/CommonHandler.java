package com.basic.task.core;

import com.basic.task.executor.LoggerExecutor;
import com.basic.task.model.ScheduleJob;
import com.basic.task.model.TaskResult;
import com.basic.task.scanner.TaskScanner;
import com.basic.task.service.WebSocketService;
import com.basic.task.task.TaskInstanceCreator;
import com.basic.task.configuration.TaskProperties;
import com.basic.task.model.TaskInfo;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Gersy
 */
@Component
public class CommonHandler {

    @Autowired
    TaskProperties taskProperties;
    @Autowired
    WebSocketService webSocketService;

    private TaskScanner taskScanner;

    private static final Map<String, TaskFlow> MAP = new ConcurrentHashMap<>();

    @Bean
    TaskScanner getTaskScanner() {
        taskScanner = new TaskScanner(taskProperties);
        return taskScanner;
    }

    public void start(JobDetail jobDetail) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        JobKey jobKey = jobDetail.getKey();
        List<TaskInfo> taskInfos = (List<TaskInfo>) jobDataMap.get("taskInfos");
        String description = " [ " + jobDataMap.get("alias") + " ] ";
        String key = ScheduleJob.createKey(jobKey.getGroup() , jobKey.getName());
        TaskResult<Object> taskResult = TaskResult.info("开始执行任务", null);
        taskResult.setJobKey(key);
        webSocketService.task(taskResult);
        TaskFlow tasks = TaskFlow.create(description)
                .tasks(TaskInstanceCreator.createTasks(taskInfos,jobKey));
        MAP.put(key, tasks);
        tasks.start(new LoggerExecutor(key, webSocketService));
        MAP.remove(key);
    }

    public void interrupt(JobKey jobKey) throws InterruptedException {
        TaskFlow taskFlow = MAP.get(ScheduleJob.createKey(jobKey.getGroup() , jobKey.getName()));
        if (taskFlow != null) {
            taskFlow.interrupt();
        }
    }

    public List<String> getGroups() {
        return taskScanner.getGroups();
    }

    public List<TaskInfo> getPublicTasks() {
        return taskScanner.getPublicTasks();
    }

    public List<TaskInfo> getGroupTasks(String group) {
        return taskScanner.getGroupTasks(group);
    }

    public Map<String, List<TaskInfo>> getTasks() {
        return taskScanner.getTasks();
    }
}
