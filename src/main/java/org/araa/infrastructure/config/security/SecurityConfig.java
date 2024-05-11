package org.araa.infrastructure.config.security;

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

@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // TODO : enable csrf for production
    private JwtAuthEntryPoint authEntryPoint;

    @Bean
    public SecurityFilterChain filterChain( HttpSecurity http ) throws Exception {
        http
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
                                .requestMatchers( "api/auth/**" ).permitAll()
                                .requestMatchers( "api/v2/authentication/**" ).permitAll()
                                .requestMatchers( "v3/api-docs/**" ).permitAll()
                                .requestMatchers( "swagger-ui/**" ).permitAll()
                                .anyRequest().authenticated()
                );

        http.addFilterBefore( jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class );
        return http.build();
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
