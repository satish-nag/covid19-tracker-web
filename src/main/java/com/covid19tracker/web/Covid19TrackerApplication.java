package com.covid19tracker.web;

import com.covid19tracker.web.filters.CorsHeaderFilter;
import com.covid19tracker.web.utils.EncryptDecryptUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import java.util.Arrays;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true,proxyTargetClass = true)
public class Covid19TrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(Covid19TrackerApplication.class, args);
    }

    @Bean
    BeanPostProcessor beanPostProcessor(){
        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                if(bean instanceof DataSourceProperties){
                    try {
                        ((DataSourceProperties) bean).setPassword(EncryptDecryptUtil.decrypt(((DataSourceProperties) bean).getPassword(),"secretKey"));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                return bean;
            }
        };
    }

    @Bean
    FilterRegistrationBean<CorsHeaderFilter> filterFilterRegistrationBean(CorsHeaderFilter corsHeaderFilter){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(corsHeaderFilter);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return filterRegistrationBean;
    }
}
