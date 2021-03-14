package com.covid19tracker.web.config;

import com.covid19tracker.web.models.GenericResponse;
import com.covid19tracker.web.utils.EncryptDecryptUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/patient/**","/admin/getHospitalList").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .anyRequest().authenticated().and().httpBasic().authenticationEntryPoint((httpServletRequest, httpServletResponse, e) -> {
                    if(e instanceof BadCredentialsException) {
                        httpServletResponse.addHeader("Content-Type","application/json");
                        httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        Map<String,String> errors = new HashMap<>();
                        errors.put("generic_error","Username and password is not correct");
                        httpServletResponse.getWriter().println(new ObjectMapper().writeValueAsString(new GenericResponse<>(null,errors)));
                        httpServletResponse.getWriter().flush();
                    }
        });
    }

    // to create DB password for admin login for application
    public static void main(String[] args) {
        if( args.length < 1 || args[0]==null || args[0].trim().isEmpty()){
            System.out.println("Password to encrypt is not provided");
            System.out.println("usage: java -jar <path_to_jar> <class_file> <password>");
            System.exit(1);
        }else {
            System.out.println(new BCryptPasswordEncoder().encode(args[0]));
        }
    }

}
