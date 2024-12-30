package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * @author ：Zc
 * @description：TODO
 * @date ：2024/12/30 14:12
 */
@Component
@Aspect
public class AutoFillAspect {

    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void pointcut() {}

    @Before("pointcut()")
    public void before(JoinPoint joinPoint) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // 获取连接点签名
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        // 获取连接点的注解对象
        AutoFill autoFill = methodSignature.getMethod().getAnnotation(AutoFill.class);
        // 获取注解对象中的元素
        OperationType operationType = autoFill.operation();
        // 获取连接点的入参实体对象
        Object[] args = joinPoint.getArgs();
        Object o = args[0];
        // 获取目标对象的set方法
        Method setCreateTime = o.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
        Method setUpdateTime = o.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
        Method setCreateUser = o.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
        Method setUpdateUser = o.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
        // 获取当前时间和账号ID
        LocalDateTime now = LocalDateTime.now();
        long id = BaseContext.getCurrentId();
        // 执行前置操作
        if (operationType == OperationType.INSERT) {
            setCreateTime.invoke(o, now);
            setUpdateTime.invoke(o, now);
            setCreateUser.invoke(o, id);
            setUpdateUser.invoke(o, id);
        } else {
            setUpdateTime.invoke(o, now);
            setUpdateUser.invoke(o, id);
        }
    }

}
