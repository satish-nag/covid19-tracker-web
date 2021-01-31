package com.covid19tracker.web.filters;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

@Component
public class CorsHeaderFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponseWrapper httpServletResponseWrapper = new HttpServletResponseWrapper((HttpServletResponse) servletResponse);
        httpServletResponseWrapper.setHeader("Access-Control-Allow-Origin", "*");
        httpServletResponseWrapper.setHeader("Access-Control-Allow-Headers", "*");
        filterChain.doFilter(servletRequest,httpServletResponseWrapper);
    }
}
