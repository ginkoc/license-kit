package com.ginko.license.checker.predicates;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author ginko
 * @date 8/28/19
 */
public class DatePredicate extends AbstractLicensePredicate {

    private static final Logger log = LoggerFactory.getLogger(DatePredicate.class);

    @Override
    public boolean test() {
        log.info("Enter datePredicate");
        Date now = new Date();
        boolean isEarly = now.before(getLicenseEffectiveDate());
        boolean isExpired = now.after(getLicenseExpiredDate());

        if (isEarly) {
            setError(PredicateError.EARLY);
        } else if (isExpired) {
            setError(PredicateError.EXPIRED);
        }

        return !isEarly && !isExpired;
    }
}
