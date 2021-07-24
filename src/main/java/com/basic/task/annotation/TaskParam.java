package com.basic.task.annotation;

import com.basic.task.constants.DataType;

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
@Target({ElementType.FIELD})
public @interface TaskParam {
    /**
     * <p>
     * 参数名称，作为paramInfoMap的key（该值可无）
     * </p>
     */
    String name() default "";

    DataType type() default DataType.STRING ;

    /**
     * 别称，用于在前端页面显示
     */
    String alias();

    /**
     * @return 默认值
     */
    String defaultValue() default "";

    /**
     * @return 可选值的选项
     */
    String[] options() default {};

}
