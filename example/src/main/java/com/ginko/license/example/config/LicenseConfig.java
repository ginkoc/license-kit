package com.ginko.license.example.config;

import com.ginko.license.checker.core.LicenseInitHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.servlet.ServletContextListener;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author ginko
 * @date 9/6/19
 */
@Component
public class LicenseConfig {

    private static final Logger log = LoggerFactory.getLogger(LicenseConfig.class);

    @Bean
    public ServletListenerRegistrationBean servletListenerRegistrationBean() {
        ServletListenerRegistrationBean<ServletContextListener>
                servletListenerRegistrationBean = new ServletListenerRegistrationBean<>();

        try {
            String properties = LicenseConfig.class.getClassLoader().getResource("license.properties").getFile();
            log.info("config path:" + properties);
            servletListenerRegistrationBean.setListener(new LicenseInitHelper(properties));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return servletListenerRegistrationBean;
    }
}
