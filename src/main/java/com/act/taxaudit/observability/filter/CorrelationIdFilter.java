package com.act.taxaudit.observability.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

/**
 * Propagates correlation IDs across services for distributed tracing.
 * Sets traceId, correlationId, and actorId in MDC for all requests.
 * MANDATORY for multi-module communication tracking in ITAS ecosystem.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorrelationIdFilter implements Filter {

    public static final String HEADER_TRACE_ID = "X-Trace-Id";
    public static final String HEADER_CORRELATION_ID = "X-Correlation-Id";
    public static final String HEADER_ACTOR_ID = "X-Actor-Id";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String traceId = httpRequest.getHeader(HEADER_TRACE_ID);
        if (traceId == null || traceId.isBlank()) {
            traceId = UUID.randomUUID().toString();
        }

        String correlationId = httpRequest.getHeader(HEADER_CORRELATION_ID);
        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }

        String actorId = httpRequest.getHeader(HEADER_ACTOR_ID);
        if (actorId == null || actorId.isBlank()) {
            actorId = "system";
        }

        MDC.put("traceId", traceId);
        MDC.put("correlationId", correlationId);
        MDC.put("actorId", actorId);

        httpResponse.setHeader(HEADER_TRACE_ID, traceId);
        httpResponse.setHeader(HEADER_CORRELATION_ID, correlationId);

        try {
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}