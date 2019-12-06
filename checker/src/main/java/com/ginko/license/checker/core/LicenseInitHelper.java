package com.ginko.license.checker.core;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author ginko
 * @date 8/27/19
 */
public final class LicenseInitHelper  implements ServletContextListener {

    private String propertiesFilePath = null;
    private LicenseContentHolder holder = LicenseContentHolder.getInstance();

    public LicenseInitHelper() {
    }

    public LicenseInitHelper(String propertiesFilePath) {
        this.propertiesFilePath = propertiesFilePath;
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        if (propertiesFilePath == null) {
            propertiesFilePath = event.getServletContext().getInitParameter("licenseProperties");

            if (propertiesFilePath == null) {
                throw new NullPointerException("LicenseProperties is null.");
            }
        }

        loadPropertiesAndSetHolderField();
        holder.install();
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        //do nothing
    }

    private void loadPropertiesAndSetHolderField() {
        File propertiesFile = new File(propertiesFilePath);
        if (!propertiesFile.exists()) {
            throw new RuntimeException("Properties file doesn't exist.");
        }

        try {
            InputStream in = new FileInputStream(propertiesFile);

            Properties properties = new Properties();
            properties.load(in);
            String subject = properties.getProperty("subject");
            String cipher = properties.getProperty("cipher");
            String filePath = properties.getProperty("path");

            //judge properties above not null

            File dir = new File(filePath);
            if (!dir.exists()) {
                throw new RuntimeException("path" + filePath + "in properties file doesn't exist");
            }

            //search .lic and .pks file
            //note that there should be one .lic file and one .pks including the dir
            File[] subFiles = dir.listFiles();
            File licenseFile = null;
            File storeFile = null;
            if (subFiles != null) {
                for (File subFile : subFiles) {
                    if (subFile.isFile()) {
                        if (subFile.getName().endsWith(".lic")) {
                            licenseFile = subFile;
                        }

                        if (subFile.getName().endsWith(".pks")) {
                            storeFile = subFile;
                        }
                    }
                }
            }

            //judge licenseFile & storeFile not null

            holder.setCipher(cipher);
            holder.setSubject(subject);
            holder.setLicenseFile(licenseFile);
            holder.setStoreFile(storeFile);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
