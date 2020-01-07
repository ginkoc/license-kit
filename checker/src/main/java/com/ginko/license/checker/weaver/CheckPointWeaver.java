package com.ginko.license.checker.weaver;

import com.ginko.license.checker.annotations.CheckPoint;
import com.ginko.license.checker.core.Checker;
import com.ginko.license.checker.exception.CheckException;
import com.ginko.license.checker.predicates.AbstractLicensePredicate;
import com.ginko.license.checker.predicates.LicensePredicate;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 一个切面，AspectJ会根据该切面进行织入
 * @author ginko
 * @date 8/28/19
 */
@Aspect
public class CheckPointWeaver {

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

        if (method.isAnnotationPresent(annotation)) {
            return method.getAnnotation(annotation);
        }

        return null;
    }
}
