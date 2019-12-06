package com.ginko.license.common.custom;

import de.schlichtherle.license.KeyStoreParam;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


/**
 * @author ginko
 * @date 7/20/19
 */
public class CustomKeyStoreParam implements KeyStoreParam {

    private final String keystorePath;
    private final String keyAlias;
    private final String keystorePwd;
    private final String keyPwd;

    public CustomKeyStoreParam(String keystorePath, String keyAlias, String keystorePwd) {
        this(keystorePath, keyAlias, keystorePwd, null);
    }

    public CustomKeyStoreParam(String keystorePath, String keyAlias, String keystorePwd, String keyPwd) {
        this.keystorePath = keystorePath;
        this.keyAlias = keyAlias;
        this.keystorePwd = keystorePwd;
        this.keyPwd = keyPwd;
    }

    @Override
    public InputStream getStream() throws IOException {
        File storeFile = new File(this.keystorePath);
        if (!storeFile.exists()) {
            throw new FileNotFoundException(String.format("Keystore file %1$s does not found", storeFile.getAbsolutePath()));
        }
        return new FileInputStream(storeFile);
    }

    @Override
    public String getAlias() {
        return keyAlias;
    }

    @Override
    public String getStorePwd() {
        return keystorePwd;
    }

    @Override
    public String getKeyPwd() {
        return keyPwd;
    }
}
