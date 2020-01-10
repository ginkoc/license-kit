package com.ginko.license.example.config;

import com.ginko.license.checker.core.LicenseInitHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContextListener;

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

        String properties = "classpath:license.properties";
        log.info("config path:" + properties);
        servletListenerRegistrationBean.setListener(new LicenseInitHelper(properties));
        return servletListenerRegistrationBean;
    }
}
