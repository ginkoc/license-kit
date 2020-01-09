package com.ginko.license.checker.predicates;

import com.ginko.license.checker.core.LicenseContentHolder;
import com.ginko.license.common.custom.CustomLicenseContent;
import com.ginko.license.common.custom.LicenseContentType;

import javax.security.auth.x500.X500Principal;
import java.util.Date;
import java.util.Optional;

/**
 * @author ginko
 * @date 8/28/19
 */
public abstract class AbstractLicensePredicate implements LicensePredicate {

    private PredicateError error = PredicateError.NONE;

    public Optional<String> getContentValueByType(LicenseContentType type) {
        return getLicenseContent().getContentValue(type);
    }

    public X500Principal getLicenseIssuer() {
        return getLicenseContent().getIssuer();
    }

    public Date getLicenseIssued() {
        return getLicenseContent().getIssued();
    }

    public Date getLicenseExpiredDate() {
        return getLicenseContent().getNotAfter();
    }

    public Date getLicenseEffectiveDate() {
        return getLicenseContent().getEffectiveDate();
    }

    public boolean isTypePresence(LicenseContentType type) {
        return getLicenseContent().getContentTypes().contains(type);
    }

    public CustomLicenseContent getLicenseContent() {
        return LicenseContentHolder.INSTANCE.getContent();
    }

    public PredicateError getError() {
        return error;
    }

    protected void setError(PredicateError error) {
        this.error = error;
    }

    @Override
    public String getErrorMessage() {
        return getError().getMessage();
    }
}
