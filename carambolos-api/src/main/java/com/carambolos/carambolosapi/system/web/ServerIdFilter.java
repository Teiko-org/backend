package com.carambolos.carambolosapi.system.web;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class ServerIdFilter implements Filter {

    public static final String SERVER_ID_HEADER = "X-Server-Id";
    private static final String serverId;

    static {
        // Prioriza variável de ambiente SERVER_ID, depois tenta hostname/IP
        String id = System.getenv("SERVER_ID");
        
        if (id == null || id.isEmpty()) {
            // Se não tiver SERVER_ID, tenta obter o hostname
            try {
                id = InetAddress.getLocalHost().getHostName();
                // Se o hostname for muito genérico ou igual ao IP, usa uma combinação
                if (id == null || id.isEmpty() || id.equals(InetAddress.getLocalHost().getHostAddress())) {
                    id = "server-" + InetAddress.getLocalHost().getHostAddress().replace(".", "-");
                }
            } catch (UnknownHostException e) {
                // Fallback: usa um ID baseado no IP
                try {
                    id = "server-" + InetAddress.getLocalHost().getHostAddress().replace(".", "-");
                } catch (UnknownHostException ex) {
                    id = "server-unknown";
                }
            }
        }
        serverId = id;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader(SERVER_ID_HEADER, serverId);
        chain.doFilter(request, response);
    }
}

