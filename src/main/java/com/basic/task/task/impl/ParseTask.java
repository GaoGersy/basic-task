package com.basic.task.task.impl;


import com.basic.task.task.impl.base.BaseTask;
import com.basic.common.auto.ApplicationContextRegister;
import com.basic.task.annotation.TaskAnnotation;
import com.basic.task.model.ParamInfo;
import com.basic.task.model.TaskInfo;
import com.basic.task.model.TaskResult;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 一种用于解析数据的Task
 *
 * @author Gersy
 * @date 2020/5/21
 */
@TaskAnnotation(group = "", name = "", ignoreScan = true)
public class ParseTask<T> extends BaseTask<T> {

    private Method method;
    private Object instance = null;
    private String error;

    public ParseTask(TaskInfo taskInfo) {
        super(taskInfo);
        try {
            init(taskInfo);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
            error = e.getMessage();
        }
    }

    private void init(TaskInfo taskInfo) throws ClassNotFoundException, NoSuchMethodException {
        Class<?> aClass = Class.forName(taskInfo.getClassName());
        try {
            instance = ApplicationContextRegister.getBean(aClass);
        } catch (Exception e) {
            try {
                instance = aClass.newInstance();
            } catch (InstantiationException | IllegalAccessException instantiationException) {
                instantiationException.printStackTrace();
            }
        }
        if (instance == null) {
            throw new RuntimeException(taskInfo.getClassName() + "获取实例失败");
        }
        method = aClass.getMethod(taskInfo.getMethodName(), Map.class);
    }

    @Override
    public TaskResult<?> execute() {
        if (error != null) {
            return TaskResult.error(taskInfo, error);
        }
        try {
            Map<String, ParamInfo> paramInfoMap = taskInfo.getParamInfoMap();
            Map<String,Object> map=new HashMap<>();
            if (paramInfoMap!=null){
                Set<Map.Entry<String, ParamInfo>> entries = paramInfoMap.entrySet();
                for (Map.Entry<String, ParamInfo> entry : entries) {
                    ParamInfo paramInfo = entry.getValue();
                    map.put(paramInfo.getName(), paramInfo.getValue());
                }
            }
            map.put("propertyInfo",propertyInfo);
            map.put("jobKey",taskInfo.getJobKey());
            Object invoke = method.invoke(instance, map);
            if (invoke==null){
                return TaskResult.success(taskInfo,"没有返回值",null);
            }
            if (invoke instanceof TaskResult) {
                return (TaskResult<?>) invoke;
            }
            return TaskResult.success(taskInfo, invoke.toString(),invoke);
        }catch (InvocationTargetException e){
            e.printStackTrace();
            return TaskResult.error(taskInfo, e.getTargetException().getCause().toString());
        } catch (Exception e) {
            e.printStackTrace();
            return TaskResult.error(taskInfo, e.getCause().toString());
        }
    }
}
