package com.carambolos.carambolosapi.system.security;

import com.carambolos.carambolosapi.application.usecases.AutenticacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguracao {

    @Autowired
    private AutenticacaoService autenticacaoService;

    @Autowired
    private JwtBlacklistFilter jwtBlacklistFilter;

    private static final AntPathRequestMatcher[] URLS_PERMITIDAS = {
            new AntPathRequestMatcher("/swagger-ui/**"),
            new AntPathRequestMatcher("/swagger-ui.html"),
            new AntPathRequestMatcher("/swagger-resources"),
            new AntPathRequestMatcher("/swagger-resources/**"),
            new AntPathRequestMatcher("/configuration/ui"),
            new AntPathRequestMatcher("/configuration/security"),
            new AntPathRequestMatcher("/api/public/**"),
            new AntPathRequestMatcher("/api/public/authenticate"),
            new AntPathRequestMatcher("/webjars/**"),
            new AntPathRequestMatcher("/v3/api-docs/**"),
            new AntPathRequestMatcher("/actuator/*"),
            new AntPathRequestMatcher("/usuarios/login", "POST"),
            new AntPathRequestMatcher("/usuarios", "POST"),
            new AntPathRequestMatcher("/usuarios", "GET"),
            new AntPathRequestMatcher("/api/usuarios/login", "POST"),
            new AntPathRequestMatcher("/api/usuarios", "POST"),
            new AntPathRequestMatcher("/api/usuarios", "GET"),
            new AntPathRequestMatcher("/h2-console/**"),
            new AntPathRequestMatcher("/h2-console/**/**"),
            new AntPathRequestMatcher("/error/**"),
            new AntPathRequestMatcher("/**", "OPTIONS"),
            // Liberar TODOS os endpoints de bolos (GET e POST) - com e sem /api/
            new AntPathRequestMatcher("/fornadas/**"),
            new AntPathRequestMatcher("/bolos/**"),
            new AntPathRequestMatcher("/decoracoes/**"),
            new AntPathRequestMatcher("/api/fornadas/**"),
            new AntPathRequestMatcher("/api/bolos/**"),
            new AntPathRequestMatcher("/api/decoracoes/**"),
            new AntPathRequestMatcher("/api/adicionais/**"),
            new AntPathRequestMatcher("/adicionais/**"),
            new AntPathRequestMatcher("/files/**"),
            new AntPathRequestMatcher("/api/files/**"),
            new AntPathRequestMatcher("/dashboard/**"),
            new AntPathRequestMatcher("/api/dashboard/**"),
            // Endereços e pedidos
            new AntPathRequestMatcher("/enderecos/**"),
            new AntPathRequestMatcher("/api/enderecos/**"),
            new AntPathRequestMatcher("/resumo-pedido/**"),
            new AntPathRequestMatcher("/api/resumo-pedido/**"),
            new AntPathRequestMatcher("/pedidos/**"),
            new AntPathRequestMatcher("/api/pedidos/**"),

    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(jwtBlacklistFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilterBean(), UsernamePasswordAuthenticationFilter.class)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .cors(Customizer.withDefaults())
                .csrf(CsrfConfigurer<HttpSecurity>::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET,
                                "/decoracoes/**",
                                "/bolos/**",
                                "/fornadas/**",
                                "/files/**",
                                "/dashboard/**",
                                "/api/decoracoes/**",
                                "/api/bolos/**",
                                "/api/fornadas/**",
                                "/api/files/**",
                                "/api/dashboard/**",
                                "/api/adicionais/**",
                                "/api/resumo-pedido/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "/bolos/**",
                                "/fornadas/**",
                                "/decoracoes/**",
                                "/enderecos/**",
                                "/resumo-pedido/**",
                                "/api/bolos/**",
                                "/api/fornadas/**",
                                "/api/decoracoes/**",
                                "/api/enderecos/**",
                                "/api/resumo-pedido/**",
                                "/api/pedidos/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.PUT,
                                "/decoracoes/**",
                                "/api/decoracoes/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.HEAD,
                                "/decoracoes/**",
                                "/bolos/**",
                                "/fornadas/**",
                                "/files/**",
                                "/dashboard/**",
                                "/api/**"
                        ).permitAll()
                        .requestMatchers(URLS_PERMITIDAS)
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint(jwtAuthenticationEntryPointBean()))
                .sessionManagement(management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(new AutenticacaoProvider(autenticacaoService, passwordEncoder()));
        return authenticationManagerBuilder.build();
    }

    @Bean
    public AutenticacaoEntryPoint jwtAuthenticationEntryPointBean() {
        return new AutenticacaoEntryPoint();
    }

    @Bean
    public AutenticacaoFilter jwtAuthenticationFilterBean() {
        return new AutenticacaoFilter(autenticacaoService, jwtAuthenticationUtilBean());
    }

    @Bean
    public GerenciadorTokenJwt jwtAuthenticationUtilBean() {
        return new GerenciadorTokenJwt();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuracao = new CorsConfiguration();
        configuracao.setAllowedOriginPatterns(List.of("*"));
        configuracao.setAllowCredentials(true);
        configuracao.setAllowedMethods(Arrays.asList(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.PATCH.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.OPTIONS.name(),
                HttpMethod.HEAD.name(),
                HttpMethod.TRACE.name()
        ));
        configuracao.setAllowedHeaders(List.of("*"));
        configuracao.setExposedHeaders(List.of(HttpHeaders.CONTENT_DISPOSITION));

        UrlBasedCorsConfigurationSource origem = new UrlBasedCorsConfigurationSource();
        origem.registerCorsConfiguration("/**", configuracao);

        return origem;
    }
}