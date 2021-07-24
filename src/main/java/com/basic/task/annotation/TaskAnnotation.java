package com.basic.task.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 任务处理的注解，用来标注任务名称及参数等信息
 *
 * @author gersy
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface TaskAnnotation {
    /**
     * 任务名称
     */
    String name();

    /**
     * 任务组名
     */
    String group();

    /**
     * 用于判定是否有文件filter选项
     */
    boolean hasFilter() default false;

    /**
     * 是否忽略扫描
     */
    boolean ignoreScan() default false;
}
