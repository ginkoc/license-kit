package com.ginko.license.checker.annotations;

import com.ginko.license.checker.predicates.AbstractLicensePredicate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ginko
 * @date 8/28/19
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckPoint {

    Class<? extends AbstractLicensePredicate>[] predicates();
}