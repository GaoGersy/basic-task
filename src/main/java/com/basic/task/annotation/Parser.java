package com.basic.task.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 解析器的注解，将带有该注解的方法包装到task执行
 *
 * @author gersy
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Parser {
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
     * 接收的参数字段
     */
    String[] params() default {};
}
