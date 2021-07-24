package com.basic.task.task;

import com.basic.task.model.PropertyInfo;
import com.basic.task.model.TaskResult;

/**
 * 对任务调度里子步骤的抽象
 *
 * @author Gersy
 */
public interface Task<T> {

    /**
     * 当任务执行完成后，会将对对应的状态及生成的结果以TaskResult的形式返回
     *
     * @return 任务执行后返回的结果及任务状态
     */
    TaskResult<?> execute();

    /**
     * 通过taskInfo向此任务传递上个任务产生的数据
     *
     * @param propertyInfo 任务信息
     */
    void setPropertyInfo(PropertyInfo<T> propertyInfo);

    /**
     * 判断是否为flat类型
     *
     * @return 为flat类型返回true
     */
    boolean isFlat();
}
