package com.carambolos.carambolosapi.system.web;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorrelationIdFilter implements Filter {

    public static final String CORRELATION_ID_HEADER = "X-Correlation-Id";
    public static final String CORRELATION_ID_MDC_KEY = "correlationId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            String correlationId = extractCorrelationId((HttpServletRequest) request);
            MDC.put(CORRELATION_ID_MDC_KEY, correlationId);
            chain.doFilter(request, response);
        } finally {
            MDC.remove(CORRELATION_ID_MDC_KEY);
        }
    }

    private String extractCorrelationId(HttpServletRequest req) {
        String header = req.getHeader(CORRELATION_ID_HEADER);
        if (header == null || header.isBlank()) {
            return UUID.randomUUID().toString();
        }
        return header;
    }
}


