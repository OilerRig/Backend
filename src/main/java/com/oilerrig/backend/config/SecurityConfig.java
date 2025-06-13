package com.oilerrig.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(r -> r
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/users/**").authenticated()
                        .requestMatchers("/test/**").permitAll()
                        .requestMatchers("/orders/**").permitAll()
                        .requestMatchers("/products/**").permitAll()
                        .requestMatchers("/docs/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs").permitAll()
                        .anyRequest().hasRole("ADMIN")
                )
                .oauth2Login(Customizer.withDefaults())
                .oauth2ResourceServer(s -> s.jwt(jwt -> jwt.jwtAuthenticationConverter(customJwtAuthenticationConverter())))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "http://127.0.0.1:5173",
                "https://delightful-wave-00b3c0b03.6.azurestaticapps.net/"
                ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept", "X-Requested-With"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public JwtAuthenticationConverter customJwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter scopeAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

        String rolesClaimName = "oilerrig/roles";

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Collection<GrantedAuthority> authorities = scopeAuthoritiesConverter.convert(jwt);

            Object rolesClaim = jwt.getClaim(rolesClaimName);

            if (rolesClaim instanceof Collection<?>) {
                Collection<String> jwtRoles = (Collection<String>) rolesClaim;
                List<GrantedAuthority> customAuthorities = jwtRoles.stream()
                        .map(role -> {
                            String prefixedRole = role.startsWith("ROLE_") ? role : "ROLE_" + role;
                            return new SimpleGrantedAuthority(prefixedRole);
                        })
                        .collect(Collectors.toList());
                authorities.addAll(customAuthorities);
            }
            else if (rolesClaim instanceof Map) {
                Map<String, Object> realmAccess = (Map<String, Object>) rolesClaim;
                Object nestedRoles = realmAccess.get("roles");
                if (nestedRoles instanceof Collection<?>) {
                    Collection<String> jwtRoles = (Collection<String>) nestedRoles;
                    List<GrantedAuthority> customAuthorities = jwtRoles.stream()
                            .map(role -> {
                                String prefixedRole = role.startsWith("ROLE_") ? role : "ROLE_" + role;
                                return new SimpleGrantedAuthority(prefixedRole);
                            })
                            .collect(Collectors.toList());
                    authorities.addAll(customAuthorities);
                }
            }
            return authorities;
        });
        return converter;
    }
}