package com.ginko.license.checker.core;

import com.ginko.license.checker.exception.CheckException;
import com.ginko.license.checker.predicates.AbstractLicensePredicate;

/**
 * @author ginko
 * @date 8/28/19
 */
public class Checker {

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
