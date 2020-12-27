package com.covid19tracker.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("test").password("{noop}test").roles("lab_tech").and()
                .withUser("test2").password("{noop}test2").roles("admin")
        ;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/patient/**").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .anyRequest().authenticated().and().httpBasic();
    }

}
