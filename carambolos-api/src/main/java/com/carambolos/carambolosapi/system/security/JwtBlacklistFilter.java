package com.carambolos.carambolosapi.system.security;

import com.carambolos.carambolosapi.application.usecases.TokenBlacklistService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtBlacklistFilter extends OncePerRequestFilter {

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String path = request.getRequestURI();

        // Endpoints públicos que não devem falhar por token em blacklist
        if (path.startsWith("/decoracoes")) {
            filterChain.doFilter(request, response);
            return;
        }
        if (("/usuarios/login".equals(path) && "POST".equalsIgnoreCase(request.getMethod())) ||
                ("/usuarios".equals(path) && "POST".equalsIgnoreCase(request.getMethod()))) {
            filterChain.doFilter(request, response);
            return;
        }

        if ("GET".equalsIgnoreCase(request.getMethod())) {
            if (path.startsWith("/dashboard") || path.startsWith("/decoracoes") || path.startsWith("/bolos") ||
                    path.startsWith("/fornadas") || path.startsWith("/resumo-pedido")) {
                filterChain.doFilter(request, response);
                return;
            }
        }
        String token = extractToken(request);

        if (token != null && tokenBlacklistService.isTokenBlacklisted(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }

        if (request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if ("authToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}