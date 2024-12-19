package com.mth.eshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public UserDetailsService userDetailsService() {
    InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
    manager.createUser(
        User.withUsername("user")
            .password(passwordEncoder().encode("password"))
            .roles("USER")
            .build());
    manager.createUser(
        User.withUsername("admin")
            .password(passwordEncoder().encode("adminPassword"))
            .roles("ADMIN")
            .build());
    return manager;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            authorize ->
                authorize
                    .requestMatchers(
                        "/", "/cart/**", "/coupon", "/item", "/item/review/add-or-update")
                    .permitAll()
                    .requestMatchers("/add", "/coupon/**", "/item/add-or-update", "/item/delete")
                    .hasRole("ADMIN")
                    .anyRequest()
                    .authenticated())
        .formLogin(Customizer.withDefaults())
        .httpBasic(Customizer.withDefaults());

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2A);
  }
}
