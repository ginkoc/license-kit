package com.ginko.license.example.customp;

import com.ginko.license.checker.predicates.AbstractLicensePredicate;
import com.ginko.license.checker.predicates.PredicateError;
import com.ginko.license.common.custom.LicenseContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * @author ginko
 * @date 9/6/19
 */
public class MyPredicate extends AbstractLicensePredicate {

    private static final Logger log = LoggerFactory.getLogger(MyPredicate.class);

    @Override
    public boolean test() {
        Optional<String> value = getContentValueByType(LicenseContentType.IP);
        String exceptIp = "127.0.0.1";
        // 如果没有设置ip属性，说明不对ip进行限制
        return !(value.isPresent() && !exceptIp.equals(value.get()));
    }

    @Override
    public String getErrorMessage() {
        return PredicateError.IP_ERROR.getMessage();
    }
}
