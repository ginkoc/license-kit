package com.ginko.license.example.customp;

import com.ginko.license.checker.predicates.AbstractLicensePredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ginko
 * @date 9/6/19
 */
public class MyPredicate extends AbstractLicensePredicate {

    private static final Logger log = LoggerFactory.getLogger(MyPredicate.class);

    @Override
    public boolean test() {
        log.info("Enter MyPredicate");
        return true;
    }
}
