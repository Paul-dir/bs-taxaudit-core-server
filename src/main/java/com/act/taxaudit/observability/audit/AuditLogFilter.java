package com.act.taxaudit.observability.audit;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Logs all API requests for multi-module compliance and transaction auditing.
 * MANDATORY for ITAS ecosystem — tracks who did what, when, and from where.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class AuditLogFilter implements Filter {

    private static final Logger auditLog = LoggerFactory.getLogger("AUDIT_LOG");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        long startTime = System.currentTimeMillis();

        try {
            chain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;

            auditLog.info(
                "AUDIT | method={} | path={} | status={} | duration={}ms | traceId={} | correlationId={} | actorId={}",
                httpRequest.getMethod(),
                httpRequest.getRequestURI(),
                httpResponse.getStatus(),
                duration,
                MDC.get("traceId"),
                MDC.get("correlationId"),
                MDC.get("actorId")
            );
        }
    }
}