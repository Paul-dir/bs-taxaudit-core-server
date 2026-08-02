package com.mor.itas.planning.observability.filter;

import jakarta.servlet.*;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(2)
public class ComponentVersionMdcFilter implements Filter {
    
    @Value("${info.app.version:unknown}")
    private String appVersion;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            MDC.put("version", appVersion);
            MDC.put("component", "bs-taxaudit-core-server");
            chain.doFilter(request, response);
        } finally {
            MDC.remove("version");
            MDC.remove("component");
        }
    }
}
