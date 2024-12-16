package org.example.inventoryservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)//pour que @preauthorize puis fonctionne
public class SecurityConfig {
    private JwtAuthConverter jwtAuthConverter;

    public SecurityConfig(JwtAuthConverter jwtAuthConverter) {
        this.jwtAuthConverter = jwtAuthConverter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                 //authentification STATELESS
                .sessionManagement(sm->sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf->csrf.disable())//on a pas besoin de protection contre les csrf car on utilise  stateless
                .headers(h->h.frameOptions(fo->fo.disable()))//authorizer les frames (fenetres)
                .authorizeRequests(ar->ar.requestMatchers( "/h2-console/**").permitAll())//authorizer ces requetes
                .authorizeRequests(ar->ar.requestMatchers( "/api/products/**").hasAuthority("ADMIN"))
                .authorizeRequests(ar->ar.anyRequest().authenticated())//toutes les requetes néccessite une authorization
// je veux protége mon service avec oauth2 , avec l utilisation de jwt
                .oauth2ResourceServer(
                o2->o2.jwt(jwt-> jwt.jwtAuthenticationConverter(jwtAuthConverter)))
                //utiliser oauth2 pour verifier signature de token et charger le profile de user
                .build();
    }

    @Bean //configuration de cross origine
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*")); // Permet toutes les origines
        configuration.setAllowedMethods(Arrays.asList("*")); // Permet toutes les méthodes HTTP
        configuration.setAllowedHeaders(Arrays.asList("*")); // Permet tous les en-têtes
        configuration.setExposedHeaders(Arrays.asList("*")); // Expose tous les en-têtes

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Applique la configuration à tous les endpoints

        return source;
    }

}

