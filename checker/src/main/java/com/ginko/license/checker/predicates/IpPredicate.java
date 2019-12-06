package com.ginko.license.checker.predicates;

import com.ginko.license.checker.utils.HardwareInfoUtils;
import com.ginko.license.common.custom.LicenseContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author ginko
 * @date 8/28/19
 */
public class IpPredicate extends AbstractLicensePredicate {

    private static final Logger log = LoggerFactory.getLogger(IpPredicate.class);

    @Override
    public boolean test() {
        Optional<String> ipLimit = getLicenseContent().getContentValue(LicenseContentType.IP);
        if (!ipLimit.isPresent()) {
            return true;
        }

        List<String> ips = Collections.emptyList();

        try {
            ips = HardwareInfoUtils.getIpAddress();
        } catch (SocketException e) {
            log.error("Get ips info failed.");
            if (log.isDebugEnabled()) {
                log.error("Exception:", e);
            }
        }

        for (String mac : ips) {
            if (ipLimit.isPresent() && ipLimit.get().equalsIgnoreCase(mac)) {
                return true;
            }
        }
        setError(PredicateError.IP_ERROR);
        return false;
    }
}
