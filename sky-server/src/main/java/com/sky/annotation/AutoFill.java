package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ：Zc
 * @description：TODO
 * @date ：2024/12/30 13:21
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AutoFill {
    // 定义一个名为operation的枚举元素，默认值为insert
    OperationType operation() default OperationType.INSERT;
}
