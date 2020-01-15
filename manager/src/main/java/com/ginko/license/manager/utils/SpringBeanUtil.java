package com.ginko.license.manager.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author ginko
 * @date 8/21/19
 */
@Component
public final class SpringBeanUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @SuppressWarnings("NullableProblems")
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringBeanUtil.applicationContext = applicationContext;
    }

    /**
     * 获取对象
     *
     * @param beanName
     * @return Object 一个以所给名字注册的bean的实例
     * @throws org.springframework.beans.BeansException
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName) {
        return (T) applicationContext.getBean(beanName);
    }

    /**
     * 获取类型为clz的对象
     *
     * @param clz
     * @return bean
     * @throws org.springframework.beans.BeansException
     */
    public static <T> T getBean(Class<T> clz) {
        return applicationContext.getBean(clz);
    }
}