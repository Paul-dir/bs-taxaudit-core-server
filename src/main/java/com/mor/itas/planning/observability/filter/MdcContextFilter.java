package com.mor.itas.planning.observability.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(1)
public class MdcContextFilter implements Filter {
    public static final String TRACE_ID_HEADER = "X-B3-TraceId";
    public static final String MDC_TRACE_ID = "traceId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            if (request instanceof HttpServletRequest req) {
                String traceId = req.getHeader(TRACE_ID_HEADER);
                if (traceId == null || traceId.isEmpty()) {
                    traceId = UUID.randomUUID().toString();
                }
                MDC.put(MDC_TRACE_ID, traceId);
            }
            chain.doFilter(request, response);
        } finally {
            MDC.remove(MDC_TRACE_ID);
        }
    }
}
