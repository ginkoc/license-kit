package com.ginko.license.checker.predicates;

import com.ginko.license.checker.utils.HardwareInfoUtils;
import com.ginko.license.common.custom.LicenseContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author ginko
 * @date 8/28/19
 */
public class MacPredicate extends AbstractLicensePredicate {

    private static final Logger log = LoggerFactory.getLogger(MacPredicate.class);

    @Override
    public boolean test() {
        Optional<String> macLimit = getContentValueByType(LicenseContentType.MAC);
        if (!macLimit.isPresent()) {
            return true;
        }

        List<String> macs = Collections.emptyList();
        try {
            macs = HardwareInfoUtils.getMacAddresses();
        } catch (Exception e) {
            log.error("Get macs info failed.");
            if (log.isDebugEnabled()) {
                log.error("Exception:", e);
            }
        }

        for (String mac : macs) {
            if (macLimit.isPresent() && macLimit.get().equalsIgnoreCase(mac)) {
                return true;
            }
        }
        setError(PredicateError.MAC_ERROR);
        return false;
    }
}
