package com.connect.acts.ActsConnectBackend.config;

import com.connect.acts.ActsConnectBackend.security.JwtAuthenticationEntryPoint;
import com.connect.acts.ActsConnectBackend.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

  @Value("${CORS_ORIGINS:http://localhost:3000}") // Default value if the environment variable is not set
  private String allowedOrigins;


  @Autowired
  public WebSecurityConfig(final JwtAuthenticationFilter jwtAuthenticationFilter, final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
    http
      .cors(cors -> cors.configurationSource(this.corsConfigurationSource()))
      .csrf(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/api/auth/**","/api/home").permitAll()
        .anyRequest().authenticated()
      )
      .exceptionHandling(exception -> exception
        .authenticationEntryPoint(this.jwtAuthenticationEntryPoint)
      )
      .addFilterBefore(this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(final AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    final CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of(this.allowedOrigins.split(","))); // Adjust your allowed origins
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Adjust methods
    configuration.setAllowedHeaders(List.of("*")); // Allow all headers
    configuration.setAllowCredentials(true); // Allow credentials
    return new UrlBasedCorsConfigurationSource() {{
        this.registerCorsConfiguration("/**", configuration);
    }};
  }
}