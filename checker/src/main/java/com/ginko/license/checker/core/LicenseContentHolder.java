package com.ginko.license.checker.core;

import com.ginko.license.common.custom.CustomKeyStoreParam;
import com.ginko.license.common.custom.CustomLicenseContent;
import de.schlichtherle.license.CipherParam;
import de.schlichtherle.license.DefaultCipherParam;
import de.schlichtherle.license.DefaultLicenseParam;
import de.schlichtherle.license.LicenseManager;
import de.schlichtherle.license.LicenseParam;

import java.io.File;
import java.util.prefs.Preferences;

/**
 * license控制信息的持有对象，需要在系统启动时进行初始化
 * @author ginko
 * @date 8/27/19
 */
public class LicenseContentHolder {

    private static final LicenseContentHolder INSTANCE = new LicenseContentHolder();
    private CustomLicenseContent content;

    private String subject;
    private String cipher;
    private File licenseFile;
    private File storeFile;

    private LicenseContentHolder() {
        if (INSTANCE != null) {
            throw new IllegalAccessError();
        }
    }

    public static LicenseContentHolder getInstance() {
        return INSTANCE;
    }

    /**
     * 安装license以获得控制信息
     * TODO: 2020/1/3  应该抛出异常，在系统启动时若果未安装成功，则启动失败
     */
    public void install() {
        assert subject != null;
        assert cipher != null;
        assert licenseFile != null;
        assert storeFile != null;

        try {
            LicenseManager lm = new LicenseManager(buildLicenseParam());
            lm.uninstall();
            content = (CustomLicenseContent) lm.install(licenseFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CustomLicenseContent getContent() {
        if (content == null) {
            throw new RuntimeException();
        }

        return content;
    }

    private LicenseParam buildLicenseParam() {
        CustomKeyStoreParam storeParam = new CustomKeyStoreParam(storeFile.toString(), cipher, cipher);
        Preferences preferences = Preferences.userNodeForPackage(LicenseContentHolder.class);
        CipherParam cipherParam = new DefaultCipherParam(cipher);
        return new DefaultLicenseParam(subject, preferences, storeParam, cipherParam);
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCipher() {
        return cipher;
    }

    public void setCipher(String cipher) {
        this.cipher = cipher;
    }

    public File getLicenseFile() {
        return licenseFile;
    }

    public void setLicenseFile(File licenseFile) {
        this.licenseFile = licenseFile;
    }

    public File getStoreFile() {
        return storeFile;
    }

    public void setStoreFile(File storeFile) {
        this.storeFile = storeFile;
    }
}
