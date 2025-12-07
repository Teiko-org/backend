package com.carambolos.carambolosapi.system.web;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class ServerIdFilter implements Filter {

    public static final String SERVER_ID_HEADER = "X-Server-Id";
    private static final String serverId;

    static {
        // Prioriza variável de ambiente SERVER_ID
        String id = System.getenv("SERVER_ID");
        
        if (id == null || id.isEmpty()) {
            // Tenta obter o IP privado da EC2 via metadata service
            String ec2PrivateIp = getEc2PrivateIp();
            if (ec2PrivateIp != null && !ec2PrivateIp.isEmpty()) {
                // Extrai o número do servidor baseado no terceiro octeto do IP
                // 10.0.2.x = server-1, 10.0.3.x = server-2
                String[] parts = ec2PrivateIp.split("\\.");
                if (parts.length >= 3) {
                    int thirdOctet = Integer.parseInt(parts[2]);
                    if (thirdOctet == 2) {
                        id = "server-1";
                    } else if (thirdOctet == 3) {
                        id = "server-2";
                    } else {
                        id = "server-" + thirdOctet;
                    }
                }
            }
            
            // Se não conseguiu via metadata, tenta HOSTNAME
            if (id == null || id.isEmpty()) {
                id = System.getenv("HOSTNAME");
                
                // Se o hostname contém o padrão EC2 (ip-10-0-X-Y), extrai o número
                if (id != null && id.startsWith("ip-10-0-")) {
                    String[] parts = id.split("-");
                    if (parts.length >= 4) {
                        int thirdOctet = Integer.parseInt(parts[3]);
                        if (thirdOctet == 2) {
                            id = "server-1";
                        } else if (thirdOctet == 3) {
                            id = "server-2";
                        } else {
                            id = "server-" + thirdOctet;
                        }
                    }
                }
            }
            
            // Se ainda não tiver, tenta ler do /etc/hostname
            if (id == null || id.isEmpty()) {
                try {
                    String hostname = Files.readString(Paths.get("/etc/hostname")).trim();
                    if (hostname.startsWith("ip-10-0-")) {
                        String[] parts = hostname.split("-");
                        if (parts.length >= 4) {
                            int thirdOctet = Integer.parseInt(parts[3]);
                            if (thirdOctet == 2) {
                                id = "server-1";
                            } else if (thirdOctet == 3) {
                                id = "server-2";
                            } else {
                                id = "server-" + thirdOctet;
                            }
                        }
                    }
                } catch (Exception e) {
                    // Ignora
                }
            }
            
            // Se ainda não tiver, tenta obter o hostname do sistema
            if (id == null || id.isEmpty()) {
                try {
                    String hostname = InetAddress.getLocalHost().getHostName();
                    if (hostname != null && hostname.startsWith("ip-10-0-")) {
                        String[] parts = hostname.split("-");
                        if (parts.length >= 4) {
                            int thirdOctet = Integer.parseInt(parts[3]);
                            if (thirdOctet == 2) {
                                id = "server-1";
                            } else if (thirdOctet == 3) {
                                id = "server-2";
                            } else {
                                id = "server-" + thirdOctet;
                            }
                        }
                    }
                } catch (UnknownHostException e) {
                    // Ignora
                }
            }
            
            // Último fallback
            if (id == null || id.isEmpty()) {
                id = "server-unknown";
            }
        }
        serverId = id;
    }
    
    private static String getEc2PrivateIp() {
        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(1))
                    .build();
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://169.254.169.254/latest/meta-data/local-ipv4"))
                    .timeout(Duration.ofSeconds(1))
                    .build();
            
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response.body().trim();
            }
        } catch (Exception e) {
            // Ignora - não está rodando na EC2 ou metadata service não disponível
        }
        return null;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader(SERVER_ID_HEADER, serverId);
        chain.doFilter(request, response);
    }
}

