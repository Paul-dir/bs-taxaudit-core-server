package com.mor.itas.planning.observability.filter;

import com.itas.bs.taxaudit.application.port.IdempotencyStorePort;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(3)
@RequiredArgsConstructor
public class IdempotencyFilter implements Filter {
    private final IdempotencyStorePort idempotencyStorePort;
    private static final String IDEMPOTENCY_KEY_HEADER = "Idempotency-Key";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest req && response instanceof HttpServletResponse res) {
            if ("POST".equalsIgnoreCase(req.getMethod())) {
                String idempotencyKey = req.getHeader(IDEMPOTENCY_KEY_HEADER);
                if (idempotencyKey != null && idempotencyStorePort.exists(idempotencyKey)) {
                    res.setStatus(HttpServletResponse.SC_CONFLICT);
                    res.getWriter().write("{\"error\": \"Duplicate Request\"}");
                    return;
                }
                // Save key handling would typically happen post-processing, but this is a stub for the structure.
            }
        }
        chain.doFilter(request, response);
    }
}
