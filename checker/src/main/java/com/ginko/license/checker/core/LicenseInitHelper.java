package com.ginko.license.checker.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 该对象用于帮助API使用者初始化license控制信息
 * @author ginko
 * @date 8/27/19
 */
public final class LicenseInitHelper  implements ServletContextListener {

    private static final Logger log = LoggerFactory.getLogger(LicenseInitHelper.class);

    private String propertiesFilePath = null;
    private String subject;
    private String cipher;
    private String licensePath;
    private String storePath;

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
                throw new NullPointerException("License properties file is null.");
            }
        }

        loadPropertiesAndSetHolderField();
        LicenseContentHolder.INSTANCE.install(subject, cipher, licensePath, storePath);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        //do nothing
    }

    /**
     * 根据配置的内容获得{@link LicenseContentHolder}初始化需要的参数
     */
    private void loadPropertiesAndSetHolderField() {
        File propertiesFile = new File(propertiesFilePath);
        if (!propertiesFile.exists()) {
            throw new RuntimeException("Properties file doesn't exist.");
        }

        Properties properties = new Properties();
        try {
            InputStream in = new FileInputStream(propertiesFile);
            properties.load(in);
        } catch (IOException e) {
            log.error("Failed to install license due to {}", e.getMessage());
            log.debug("Exception", e);
            throw new RuntimeException("Failed to load properties due to " + e);
        }

        subject = properties.getProperty("subject");
        cipher = properties.getProperty("cipher");
        String filePath = properties.getProperty("path");

        File dir = new File(filePath);
        if (!dir.exists()) {
            throw new RuntimeException("File" + filePath + "in properties doesn't exist.");
        }

        // 要指定文件名不太现实，暂时还是强制要求指定目录下只能有一个.lic和.pks文件
        File[] subFiles = dir.listFiles();
        if (subFiles != null) {
            for (File subFile : subFiles) {
                if (subFile.isFile()) {
                    if (subFile.getName().endsWith(".lic")) {
                        licensePath = subFile.getAbsolutePath();
                    }

                    if (subFile.getName().endsWith(".pks")) {
                        storePath = subFile.getAbsolutePath();
                    }
                }
            }
        }
    }
}
