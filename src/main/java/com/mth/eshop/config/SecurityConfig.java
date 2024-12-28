package com.mth.eshop.config;

import com.mth.eshop.repository.UserRepository;
import com.mth.eshop.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public UserDetailsService userDetailsService(UserRepository userRepository) {
    return new CustomUserDetailsService(userRepository);
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            authorize ->
                authorize
                    .requestMatchers(
                        "/**",
                        "/cart/**",
                        "/coupon",
                        "/item",
                        "/item/review/add-or-update",
                        "/user/login",
                        "/user/register")
                    .permitAll()
                    .requestMatchers("/user/**")
                    .hasRole("CUSTOMER")
                    .requestMatchers("/coupon/**", "/item/add-or-update", "/item/delete")
                    .hasRole("ADMIN")
                    .anyRequest()
                    .authenticated())
        .formLogin(
            login ->
                login
                    .loginProcessingUrl("/user/login")
                    .successHandler(
                        (request, response, authentication) -> {
                          response.setContentType("application/json");
                          response.setStatus(HttpStatus.OK.value());
                          response
                              .getWriter()
                              .write(
                                  "{\"status\": \"success\", \"message\": \"Login successful\"}");
                          response.getWriter().flush();
                        })
                    .failureHandler(
                        (request, response, exception) -> {
                          response.setContentType("application/json");
                          response.setStatus(HttpStatus.UNAUTHORIZED.value());
                          response
                              .getWriter()
                              .write("{\"status\": \"error\", \"message\": \"Login failed\"}");
                          response.getWriter().flush();
                        }))
        .logout(
            logout ->
                logout
                    .logoutUrl("/user/logout")
                    .logoutSuccessHandler(
                        (request, response, authentication) -> {
                          response.setContentType("application/json");
                          response.setStatus(HttpStatus.OK.value());
                          response
                              .getWriter()
                              .write(
                                  "{\"status\": \"success\", \"message\": \"Logout successful\"}");
                          response.getWriter().flush();
                        }))
        .sessionManagement(session -> session.maximumSessions(1).maxSessionsPreventsLogin(false))
        .httpBasic(Customizer.withDefaults());

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2A);
  }
}
