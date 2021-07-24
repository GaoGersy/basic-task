package com.basic.task.scanner;

import com.basic.common.utils.ClassUtils;
import com.basic.task.annotation.Parser;
import com.basic.task.annotation.TaskAnnotation;
import com.basic.task.annotation.TaskParam;
import com.basic.task.configuration.TaskProperties;
import com.basic.task.constants.Constants;
import com.basic.task.model.ParamInfo;
import com.basic.task.model.TaskInfo;
import com.basic.task.task.Task;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Gersy
 * @date 2020/6/12
 */
public class TaskScanner {

    private static final String PARAM_SPLINTER = ":";

    private final TaskProperties taskProperties;
    private static final ConcurrentHashMap<String, List<TaskInfo>> TASK_GROUP_MAP = new ConcurrentHashMap<String, List<TaskInfo>>();

    public TaskScanner(TaskProperties taskProperties) {
        this.taskProperties = taskProperties;
        scanLocalTask();
        String[] taskPackages = taskProperties.getTaskPackages();
        if (taskPackages != null && taskPackages.length > 0) {
            for (String taskPackage : taskPackages) {
                taskPackage = taskPackage.trim();
                getTasksWithPackage(taskPackage);
            }
        }
    }

    private void scanLocalTask() {
        Class<Task> taskClass = Task.class;
        getTasksWithPackage(taskClass.getPackage().getName());
    }

    /**
     * @return @TaskAnnotation、@Parser 中group定义为Constants.PUBLIC的任务
     */
    public List<TaskInfo> getPublicTasks() {
        return getGroupTasks(Constants.PUBLIC);
    }

    /**
     * @return 所有的任务组名列表
     */
    public List<String> getGroups() {
        return Collections.list(TASK_GROUP_MAP.keys());
    }

    /**
     * @param group 组名
     * @return 组名下的任务列表
     */
    public List<TaskInfo> getGroupTasks(String group) {
        return TASK_GROUP_MAP.get(group);
    }

    public Map<String, List<TaskInfo>> getTasks() {
        return TASK_GROUP_MAP;
    }

    /**
     * @param condition 搜索条件
     * @return 通过正则匹配到的任务名列表
     */
    public List<TaskInfo> search(String condition) {
        Pattern pattern = getPattern(condition);
        List<TaskInfo> results = new ArrayList<>();
        Set<Map.Entry<String, List<TaskInfo>>> entries = TASK_GROUP_MAP.entrySet();
        for (Map.Entry<String, List<TaskInfo>> entry : entries) {
            List<TaskInfo> taskInfos = entry.getValue();
            for (TaskInfo taskInfo : taskInfos) {
                Matcher matcher = pattern.matcher(taskInfo.getTaskName());
                if (matcher.find()) {
                    results.add(taskInfo);
                }
            }
        }
        return results;
    }

    private static final String PATTERN = ".*#.*";

    private Pattern getPattern(String condition) {
        return Pattern.compile(PATTERN.replace("#", condition));
    }

    /**
     * 扫描某个包下的任务
     *
     * @param packageName 包名全路径
     */
    private void getTasksWithPackage(String packageName) {
        getClassTasks(packageName);
        getMethodClassTasks(packageName);
    }

    /**
     * 扫描类上的@TaskAnnotation注解的任务
     *
     * @param packageName 包名
     */
    private void getClassTasks(String packageName) {
        Class<TaskAnnotation> taskAnnotation = TaskAnnotation.class;
        Set<Class<? extends Task>> annotationClasses = ClassUtils.getSubClasses(packageName, Task.class);
        for (Class<? extends Task> aClass : annotationClasses) {
            TaskAnnotation annotation = aClass.getAnnotation(taskAnnotation);
            if (annotation == null) {
                boolean anAbstract = Modifier.isAbstract(aClass.getModifiers());
                if (anAbstract) {
                    continue;
                } else {
                    throw new RuntimeException(aClass + " 需要加上@TaskAnnotation注解，标记任务名等信息");
                }
            }
            //忽略扫描
            if (annotation.ignoreScan()) {
                continue;
            }
            String name = annotation.name();
            boolean b = annotation.hasFilter();
            String group = annotation.group();
//            boolean isPublic = isPublicTask(group);
            TaskInfo taskInfo = new TaskInfo(
                    name,
                    group,
                    aClass.getName(),
                    b ? new ArrayList<>() : null,
                    getParamMap(aClass)
            );
            List<TaskInfo> taskInfos = TASK_GROUP_MAP.computeIfAbsent(group, g -> new ArrayList<>());
            taskInfos.add(taskInfo);
        }
    }

    private boolean isPublicTask(String group) {
        return Constants.PUBLIC.equals(group);
    }

    private Map<String, ParamInfo> getParamMap(Class<? extends Task> clazz) {
        Class<TaskParam> taskParamClass = TaskParam.class;
        Field[] fields = clazz.getDeclaredFields();
        Map<String, ParamInfo> map = null;
        for (Field field : fields) {
            TaskParam annotation = field.getAnnotation(taskParamClass);
            if (annotation != null) {
                if (map == null) {
                    map = new LinkedHashMap<>();
                }
                ParamInfo paramInfo = new ParamInfo(field.getName(), annotation.alias());
                paramInfo.setType(annotation.type().getType());
                map.put(field.getName(), paramInfo);
            }
        }
        return map;
    }

    /**
     * 扫描方法上注解@Parser的任务
     *
     * @param packageName 包名
     */
    private void getMethodClassTasks(String packageName) {
        Class<Parser> parserClass = Parser.class;
        Set<Method> annotationClasses = ClassUtils.getMethodsAnnotationClasses(packageName, parserClass);
        for (Method method : annotationClasses) {
            Class<?> declaringClass = method.getDeclaringClass();
            if(declaringClass.isInterface()){
                //如果注解写在接口方法上则跳过
                continue;
            }
            Parser annotation = method.getAnnotation(parserClass);
            String name = annotation.name();
            boolean b = annotation.hasFilter();
            String group = annotation.group();
//            boolean isPublic = isPublicTask(group);
            String[] params = annotation.params();
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length == 1) {
                Class<?> parameterType = parameterTypes[0];
                if (parameterType != Map.class) {
                    throw new RuntimeException("@Parser注解的方法只能有一个参数，参数类型为 Map<String,Object> ");
                }
            } else {
                throw new RuntimeException("@Parser注解的方法只能有一个参数，参数类型为 Map<String,Object>");
            }
            Map<String, ParamInfo> map = null;
            if (params.length > 0) {
                map = new LinkedHashMap<>(params.length);
                for (String param : params) {
//                    String alias = taskProperties.getAlias(param);
//                    alias = alias == null ? param : alias;
                    String alias = param;
                    if (param.contains(PARAM_SPLINTER)) {
                        int index = param.indexOf(PARAM_SPLINTER);
                        alias = param.substring(index + 1);
                        param = param.substring(0, index);
                    }

                    ParamInfo paramInfo = new ParamInfo(param, alias);
                    map.put(param, paramInfo);
                }
            }
            TaskInfo taskInfo = new TaskInfo(
                    true,
                    name,
                    group,
                    method.getDeclaringClass().getName(),
                    method.getName(),
                    b ? new ArrayList<>() : null,
                    map
            );
            List<TaskInfo> taskInfos = TASK_GROUP_MAP.computeIfAbsent(group, g -> new ArrayList<>());
            taskInfos.add(taskInfo);
        }
    }
}
