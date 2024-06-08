package org.araa.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private JwtAuthEntryPoint authEntryPoint;

    @Bean
    public SecurityFilterChain filterChain( HttpSecurity http ) throws Exception {
        http
                .cors( cors -> cors.configurationSource( corsConfigurationSource() ) )
                .csrf( CsrfConfigurer::disable )
                .securityContext( securityContext ->
                        securityContext
                                .securityContextRepository(
                                        new DelegatingSecurityContextRepository(
                                                new RequestAttributeSecurityContextRepository(),
                                                new HttpSessionSecurityContextRepository()
                                        )
                                ) )
                .exceptionHandling( exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint( authEntryPoint )
                )
                .sessionManagement( sessionManagement ->
                        sessionManagement
                                .sessionCreationPolicy( SessionCreationPolicy.STATELESS )
                )
                .authorizeHttpRequests( requests ->
                        requests
                                .requestMatchers( "api/v2/auth/**" ).permitAll()
                                .requestMatchers( "v3/api-docs/**" ).permitAll()
                                .requestMatchers( "swagger-ui/**" ).permitAll()
                                .requestMatchers( "/error" ).permitAll()
                                .requestMatchers( "/health" ).permitAll()
                                .requestMatchers( "/api/v2/role/**" ).hasRole( "ADMIN" )
                                .anyRequest().authenticated()
                );

        http.addFilterBefore( jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class );
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins( List.of("http://localhost:5173")); // Adjust this to specify the actual origins
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration ) throws Exception {
        return configuration.getAuthenticationManager();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter();
    }

}
