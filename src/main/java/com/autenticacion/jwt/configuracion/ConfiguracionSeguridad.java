package com.autenticacion.jwt.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.autenticacion.jwt.JwtAccesoDenegadoError;
import com.autenticacion.jwt.JwtAutenticacionError;
import com.autenticacion.jwt.JwtFiltroPeticiones;
import com.autenticacion.servicios.DetalleUsuarioImpl;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class ConfiguracionSeguridad {

    private final DetalleUsuarioImpl detalleUsuarioImpl;
    private final JwtAutenticacionError jwtAutenticacionError;
    private final JwtAccesoDenegadoError jwtAccesoDenegadoError;
    private final JwtFiltroPeticiones jwtFiltroPeticiones;

    public ConfiguracionSeguridad(
            DetalleUsuarioImpl detalleUsuarioImpl,
            JwtAutenticacionError jwtAutenticacionError,
            JwtAccesoDenegadoError jwtAccesoDenegadoError,
            JwtFiltroPeticiones jwtFiltroPeticiones) {
        this.detalleUsuarioImpl = detalleUsuarioImpl;
        this.jwtAutenticacionError = jwtAutenticacionError;
        this.jwtAccesoDenegadoError = jwtAccesoDenegadoError;
        this.jwtFiltroPeticiones = jwtFiltroPeticiones;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
            .authorizeHttpRequests(authConfig -> {
                authConfig.requestMatchers("/usuario/login", "/usuario/crear").permitAll(); // Endpoints públicos
                authConfig.anyRequest().authenticated(); // Todo lo demás requiere autenticación
            })
            .exceptionHandling(exception -> 
                exception.authenticationEntryPoint(jwtAutenticacionError)
                        .accessDeniedHandler(jwtAccesoDenegadoError)
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .userDetailsService(detalleUsuarioImpl)
            .addFilterBefore(jwtFiltroPeticiones, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
