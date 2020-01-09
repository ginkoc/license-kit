package com.ginko.license.manager.parameters;

import com.ginko.license.common.custom.CustomLicenseContent;
import com.ginko.license.manager.contants.Constants;
import de.schlichtherle.license.LicenseContent;

import javax.security.auth.x500.X500Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.prefs.Preferences;

/**
 * @author ginko
 * @date 7/24/19
 */
public class CreateLicenseParameters {

    private String cipher;
    private String subject;
    private String licenseFile;
    private Preferences preferences;
    private LicenseContent licenseContent;

    public CreateLicenseParameters(String cipher,
                                   String subject,
                                   String licenseFile,
                                   boolean defaultContent) {
        assert cipher != null;
        assert subject != null;
        this.cipher = cipher;
        this.subject = subject;
        this.licenseFile = licenseFile;
        if (defaultContent) {
            this.licenseContent = defaultLicenseContent();
        }
    }

    public String getCipher() {
        return cipher;
    }

    public void setCipher(String cipher) {
        this.cipher = cipher;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Preferences getPreferences() {
        return preferences;
    }

    public void setPreferences(Preferences preferences) {
        if (!preferences.isUserNode()) {
            throw new IllegalArgumentException("The preference can only be userNode");
        }
        this.preferences = preferences;
    }

    public String getLicenseFile() {
        return licenseFile;
    }

    public void setLicenseFile(String licenseFile) {
        this.licenseFile = licenseFile;
    }

    public LicenseContent getLicenseContent() {
        return licenseContent;
    }

    public void setLicenseContent(LicenseContent licenseContent) {
        if (!validateLicenseContent(licenseContent)) {
            throw new IllegalArgumentException("LicenseContent's consumerType must be User," +
                    " consumerAmount must be 1 and subject must equals which of parameters");
        }
        this.licenseContent = licenseContent;
    }

    private LicenseContent defaultLicenseContent() {
        CustomLicenseContent content = new CustomLicenseContent();
        content.setHolder(new X500Principal(Constants.DEFAULT_HOLDER));
        content.setSubject(getSubject());
        content.setConsumerType(Constants.LICENSE_CONSUMER_TYPE);
        content.setConsumerAmount(Constants.LICENSE_CONSUMER_AMOUNT);
        content.setIssuer(new X500Principal(Constants.DEFAULT_ISSUER));
        Date date = new Date();
        content.setIssued(date);
        content.setNotAfter(Date.from(LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).plusYears(10)
                .atZone(ZoneId.systemDefault()).toInstant()));
        return content;
    }

    private boolean validateLicenseContent(LicenseContent licenseContent) {
        return Constants.LICENSE_CONSUMER_TYPE.equals(licenseContent.getConsumerType()) &&
                licenseContent.getConsumerAmount() == 1 &&
                getSubject().equals(licenseContent.getSubject());
    }
}
