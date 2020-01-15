package com.ginko.license.checker.weaver;

import com.ginko.license.checker.annotations.CheckPoint;
import com.ginko.license.checker.core.Checker;
import com.ginko.license.checker.exception.CheckException;
import com.ginko.license.checker.predicates.LicensePredicate;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 一个切面，AspectJ会根据该切面进行织入
 *
 * @author ginko
 * @date 8/28/19
 */
@Aspect
public class CheckPointWeaver {

    private static final Logger log = LoggerFactory.getLogger(CheckPointWeaver.class);

    @Pointcut("execution(* *(..)) && @annotation(com.ginko.license.checker.annotations.CheckPoint)")
    public void jointPoint() {
    }

    @Before("jointPoint()")
    public void before(JoinPoint joinPoint) throws CheckException {
        CheckPoint checkPoint = getAnnotationByClass(joinPoint, CheckPoint.class);
        if (checkPoint == null) {
            return;
        }

        Class<? extends LicensePredicate>[] predicates = checkPoint.predicates();
        Checker.check(predicates);
    }

    private <T extends Annotation> T getAnnotationByClass(JoinPoint joinPoint, Class<T> annotation) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        try {
            // 防止方法通过动态代理或者cglib代理后无法获得真实的注解
            Method realMethod = joinPoint.getTarget().getClass().getDeclaredMethod(signature.getName(), method.getParameterTypes());
            return realMethod.isAnnotationPresent(annotation) ? realMethod.getAnnotation(annotation) : null;
        } catch (NoSuchMethodException e) {
            log.error("Get method" + method + "annotations failed!", e);
            throw new RuntimeException("Get method" + method + "annotations failed!");
        }
    }
}
