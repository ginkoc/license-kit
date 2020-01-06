package com.ginko.license.checker.core;

import com.ginko.license.checker.exception.CheckException;
import com.ginko.license.checker.predicates.AbstractLicensePredicate;

/**
 * 对校验逻辑的封装
 * @author ginko
 * @date 8/28/19
 */
public class Checker {


    /**
     * 对所有传入的{@link AbstractLicensePredicate}进行校验
     * 需要注意传入可变参数量至少大于1
     * // TODO: 2020/1/6  把AbstractLicensePredicate修改为LicensePredicate
     */
    @SafeVarargs
    public static void check(Class<? extends AbstractLicensePredicate> ...predicates) throws CheckException {
        if (predicates == null) {
            throw new IllegalArgumentException("Predicates couldn't be null.");
        }

        for (Class<? extends AbstractLicensePredicate> clazz : predicates) {
            AbstractLicensePredicate predicate;
            try {
                predicate = clazz.newInstance();
            } catch (Exception e) {
                throw new RuntimeException();
            }

            if (!predicate.test()) {
                throw new CheckException(predicate.getError().getMessage(), predicate.getError());
            }
        }
    }
}
