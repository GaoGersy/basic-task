package com.basic.task.task.impl.base;

import com.basic.task.annotation.TaskParam;
import com.basic.task.model.ParamInfo;
import com.basic.task.model.PropertyInfo;
import com.basic.task.model.TaskInfo;
import com.basic.task.task.Task;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author Gersy
 */
public abstract class BaseTask<T> implements Task<T> {
    /**
     * 上个任务传递过来的数据
     */
    protected PropertyInfo<T> propertyInfo;
    protected TaskInfo taskInfo;
    /**
     * 标记是否为FlatTask
     */
    protected boolean isFlat;

    public BaseTask(TaskInfo taskInfo) {
        this.taskInfo = taskInfo;
        param2Field(taskInfo.getParamInfoMap());
    }


    @Override
    public void setPropertyInfo(PropertyInfo<T> propertyInfo) {
        this.propertyInfo = propertyInfo;
    }

    @Override
    public boolean isFlat() {
        return isFlat;
    }

    public void setFlat(boolean flat) {
        isFlat = flat;
    }

    public void setTaskInfo(TaskInfo taskInfo) {
        this.taskInfo = taskInfo;
    }

    protected void param2Field(Map<String, ParamInfo> paramMap) {
        if (paramMap == null) {
            return;
        }
        Class<TaskParam> taskParamClass = TaskParam.class;
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            TaskParam annotation = field.getAnnotation(taskParamClass);
            if (annotation != null) {
                String name = field.getName();
                String o = paramMap.get(name).getValue();
                field.setAccessible(true);
                try {
                    setValue(field, o);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setValue(Field field, String o) throws IllegalAccessException {
        Class<?> type = field.getType();
        if (type == Boolean.class || type == boolean.class) {
            field.set(this, Boolean.parseBoolean(o));
        } else if (type == Integer.class || type == int.class) {
            field.set(this, Integer.parseInt(o));
        } else if (type == Long.class || type == long.class) {
            field.set(this, Long.parseLong(o));
        } else if (type == Double.class || type == double.class) {
            field.set(this, Double.parseDouble(o));
        } else if (type == Float.class || type == float.class) {
            field.set(this, Float.parseFloat(o));
        } else {
            field.set(this, o);
        }
    }
}
