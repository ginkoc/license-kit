package com.ginko.license.common.custom;

import de.schlichtherle.license.LicenseContent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


/**
 * @author ginko
 * @date 7/20/19
 */
public class CustomLicenseContent extends LicenseContent {

    private static final long serialVersionUID = 3394987441801191249L;

    private final Map<LicenseContentType, String> contentValueMap = new HashMap<>();

    public Optional<String> getContentValue(LicenseContentType key) {
        return Optional.ofNullable(contentValueMap.get(key));
    }

    public void setContentValue(LicenseContentType key, String val) {
        contentValueMap.put(key, val);
    }

    public Set<LicenseContentType> getContentTypes() {
        return contentValueMap.keySet();
    }
}
