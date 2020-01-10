package com.ginko.license.example.config;

import com.ginko.license.checker.core.LicenseInitHelper;
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

    @Bean
    public ServletListenerRegistrationBean servletListenerRegistrationBean() {
        ServletListenerRegistrationBean<ServletContextListener>
                servletListenerRegistrationBean = new ServletListenerRegistrationBean<>();

        // 以'classpath:'代表从jar包的classes目录下读取配置文件，否则表示从文件系统读取配置文件
        String properties = "classpath:license.properties";
        servletListenerRegistrationBean.setListener(new LicenseInitHelper(properties));
        return servletListenerRegistrationBean;
    }
}
