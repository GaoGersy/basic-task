package com.basic.task.task;

import com.basic.task.model.ScheduleJob;
import com.basic.task.task.impl.base.BaseTask;
import com.basic.task.model.TaskInfo;
import com.basic.task.task.impl.ParseTask;

import org.quartz.JobKey;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Gersy
 * @date 2020/5/21
 */
public class TaskInstanceCreator {

    public static List<Task<?>> createTasks(List<TaskInfo> taskInfos, JobKey jobKey) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        if (taskInfos==null){
            return new ArrayList<>();
        }
        String key = ScheduleJob.createKey(jobKey.getGroup(),jobKey.getName());
        List<Task<?>> tasks = new ArrayList<>(taskInfos.size());
        for (TaskInfo taskInfo : taskInfos) {
            taskInfo.setJobKey(key);
            tasks.add(createTask(taskInfo));
        }
        return tasks;
    }

    public static <T> Task<T> createTask(TaskInfo taskInfo) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
            if (taskInfo.isParserTask()){
                return createParserTask(taskInfo);
            }else {
                return createNormalTask(taskInfo);
            }
    }

    private static <T> BaseTask<T> createNormalTask(TaskInfo taskInfo) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Class<?> aClass = Class.forName(taskInfo.getClassName());
        Constructor<?> c = aClass.getConstructor(TaskInfo.class);
        BaseTask<T> baseTask = (BaseTask<T>) c.newInstance(taskInfo);
        return baseTask;
    }

    public static <T> Task<T> createParserTask(TaskInfo taskInfo) {
        return new ParseTask<>(taskInfo);
    }
}
