package com.ginko.license.checker.annotations;

import com.ginko.license.checker.predicates.LicensePredicate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 校验标记
 *
 * @author ginko
 * @date 8/28/19
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckPoint {

    Class<? extends LicensePredicate>[] predicates();
}