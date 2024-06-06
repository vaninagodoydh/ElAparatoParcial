package com.elaparato.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class OAuth2ResourceServerSecurityConfiguration {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeyCloakJwtAuthenticationConverter());

    http.
            authorizeHttpRequests(
                    auth -> auth
                            //ROLE_vendedor
                            .requestMatchers(HttpMethod.GET, "/ventas/**").hasAnyRole("vendedor", "admin")
//                            .requestMatchers(HttpMethod.GET, "/productos/**").hasAnyRole("repositor", "admin")
//                            .requestMatchers(HttpMethod.GET, "/users/**").hasAnyRole("admin")
                            .anyRequest().authenticated()
            )
            .oauth2ResourceServer(
                    oauth -> oauth.jwt().jwtAuthenticationConverter(jwtAuthenticationConverter)
            );
    return http.build();
  }
}
