package com.ginko.license.checker.core;

import com.ginko.license.common.custom.CustomKeyStoreParam;
import com.ginko.license.common.custom.CustomLicenseContent;
import de.schlichtherle.license.CipherParam;
import de.schlichtherle.license.DefaultCipherParam;
import de.schlichtherle.license.DefaultLicenseParam;
import de.schlichtherle.license.LicenseManager;
import de.schlichtherle.license.LicenseParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.prefs.Preferences;

/**
 * license控制信息的持有对象，用枚举类实现的单例，需要在系统启动时进行初始化
 *
 * @author ginko
 * @date 8/27/19
 */
public enum LicenseContentHolder {
    /**
     * 枚举单例对象
     */
    INSTANCE;

    private static final Logger log = LoggerFactory.getLogger(LicenseContentHolder.class);

    private CustomLicenseContent content;
    private String subject;
    private String cipher;
    private String storePath;
    private File licenseFile;

    /**
     * 安装license以获得控制信息
     */
    public void install(final String subject,
                        final String cipher,
                        final String licensePath,
                        final String storePath) {
        validate(subject, cipher, licensePath, storePath);

        try {
            LicenseManager lm = new LicenseManager(buildLicenseParam());
            lm.uninstall();
            content = (CustomLicenseContent) lm.install(licenseFile);
        } catch (Exception e) {
            log.error("Failed to install license due to {}", e.getMessage());
            log.debug("Exception", e);
            throw new RuntimeException("Failed to install license due to " + e);
        }
    }

    public CustomLicenseContent getContent() {
        if (content == null) {
            throw new RuntimeException("Content is null, maybe didn't installed license or installed fail.");
        }

        return content;
    }

    private LicenseParam buildLicenseParam() {
        CustomKeyStoreParam storeParam = new CustomKeyStoreParam(storePath, cipher, cipher);
        Preferences preferences = Preferences.userNodeForPackage(LicenseContentHolder.class);
        CipherParam cipherParam = new DefaultCipherParam(cipher);
        return new DefaultLicenseParam(subject, preferences, storeParam, cipherParam);
    }

    private void validate(final String subject,
                          final String cipher,
                          final String licensePath,
                          final String storePath) {
        if (subject != null && subject.length() > 0) {
            this.subject = subject;
        } else {
            throw new RuntimeException("Can't install license when subject is null!");
        }

        if (cipher != null && cipher.length() > 0) {
            this.cipher = cipher;
        } else {
            throw new RuntimeException("Can't install license when cipher is null!");
        }

        if (licensePath != null && licensePath.length() > 0) {
            File licenseFile = new File(licensePath);
            if (licenseFile.exists()) {
                this.licenseFile = licenseFile;
            } else {
                throw new RuntimeException("Can't install license when license file doesn't exist!");
            }
        } else {
            throw new RuntimeException("Can't install license when license file is null!");
        }

        if (storePath != null && storePath.length() > 0) {
            File keyStore = new File(storePath);
            if (keyStore.exists()) {
                this.storePath = storePath;
            } else {
                throw new RuntimeException("Can't install license when key store doesn't exist!");
            }
        } else {
            throw new RuntimeException("Can't install license when key store is null!");
        }
    }

    public String getSubject() {
        return subject;
    }

    public String getCipher() {
        return cipher;
    }

    public File getLicenseFile() {
        return licenseFile;
    }

    public String getStorePath() {
        return storePath;
    }
}
