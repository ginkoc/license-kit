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
 * 该对象用于帮助API使用者初始化license控制信息
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
        // TODO: 2020/1/3 将holder修改为成员变量
        holder.install();
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        //do nothing
    }

    /**
     * 根据配置的内容获得{@link LicenseContentHolder}初始化需要的参数
     * TODO: 2020/1/3  holder初始需要的参数在这里赋值有些不太合适，这里应该只进行参数的获取，并且将properties参数传给install方法
     */
    private void loadPropertiesAndSetHolderField() {
        File propertiesFile = new File(propertiesFilePath);
        if (!propertiesFile.exists()) {
            throw new RuntimeException("Properties file doesn't exist.");
        }

        try {
            InputStream in = new FileInputStream(propertiesFile);

            // TODO: 2020/1/3 增加参数是否正确的校验逻辑
            Properties properties = new Properties();
            properties.load(in);
            String subject = properties.getProperty("subject");
            String cipher = properties.getProperty("cipher");
            String filePath = properties.getProperty("path");

            File dir = new File(filePath);
            if (!dir.exists()) {
                throw new RuntimeException("path" + filePath + "in properties file doesn't exist");
            }

            // TODO: 2020/1/3 这样的的逻辑要求指定目录下只能有1个.lic和1个.pks文件有点太严苛了,应该修改这里的逻辑，并且判断文件是否存在
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


            // TODO: 2020/1/3 应当移动到holder的install方法中
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
