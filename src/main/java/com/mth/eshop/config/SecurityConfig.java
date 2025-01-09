package com.mth.eshop.config;

import com.mth.eshop.repository.UserRepository;
import com.mth.eshop.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public UserDetailsService userDetailsService(UserRepository userRepository) {
    return new CustomUserDetailsService(userRepository);
  }

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public CustomAuthenticationFilter customAuthenticationFilter(AuthenticationManager authenticationManager) {
    CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter();
    customAuthenticationFilter.setAuthenticationManager(authenticationManager);
    customAuthenticationFilter.setFilterProcessesUrl("/user/login");
    return customAuthenticationFilter;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager)
      throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .anonymous(AbstractHttpConfigurer::disable)
        .sessionManagement(
            session ->
                session
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .sessionFixation()
                    .migrateSession()
                    .maximumSessions(1)
                    .maxSessionsPreventsLogin(true)
                    .expiredSessionStrategy(
                        sessionInformationExpiredEvent -> {
                          sessionInformationExpiredEvent
                              .getResponse()
                              .sendError(HttpStatus.UNAUTHORIZED.value(), "Session expired");
                        })
                    .expiredUrl("/user/login"))
        .authorizeHttpRequests(
            authorize ->
                authorize
                    .requestMatchers(
                        "/",
                        "/{userId}/{cartId}",
                        "/user/login",
                        "/user/register/{customerId}/{cartId}",
                        "/cart/**",
                        "/coupon",
                        "/coupon/{id}",
                        "/item/{id}",
                        "/item/{id}/review/add-or-update")
                    .permitAll()
                    .requestMatchers(
                        "/user/details", "/user/update", "/user/update", "/user/logout")
                    .hasAnyRole("USER", "ADMIN")
                    .requestMatchers(
                        "/coupon/add",
                        "/coupon/delete/{id}",
                        "/item/add-or-update",
                        "/item/delete/{id}")
                    .hasRole("ADMIN")
                    .anyRequest()
                    .authenticated())
        .addFilterBefore(customAuthenticationFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class)
        .rememberMe(
            rememberMe ->
                rememberMe
                    .tokenValiditySeconds(7 * 24 * 60 * 60)
                    .rememberMeParameter("stayLoggedIn")
                    .key("mThEshopKey")
                    .tokenRepository(persistentTokenRepository()))
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
                        })
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .deleteCookies("JSESSIONID", "remember-me")
                    .permitAll())
        .httpBasic(Customizer.withDefaults());

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2A);
  }

  @Bean
  public PersistentTokenRepository persistentTokenRepository() {
    return new InMemoryTokenRepositoryImpl();
  }
}
