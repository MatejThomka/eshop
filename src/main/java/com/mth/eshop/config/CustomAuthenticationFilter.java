package com.mth.eshop.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    try {
      Map<String, String> credentials =
          objectMapper.readValue(
              request.getInputStream(), new TypeReference<Map<String, String>>() {});
      String email = credentials.get("email");
      String password = credentials.get("password");

      UsernamePasswordAuthenticationToken authRequest =
          new UsernamePasswordAuthenticationToken(email, password);
      return this.getAuthenticationManager().authenticate(authRequest);
    } catch (IOException e) {
      throw new RuntimeException("Invalid login request format");
    }
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authResult)
      throws IOException {

    SecurityContextHolder.getContext().setAuthentication(authResult);
    securityContextRepository.saveContext(SecurityContextHolder.getContext(), request, response);

    response.setContentType("application/json");
    response.setStatus(HttpStatus.OK.value());
    response.getWriter().write("{\"status\": \"success\", \"message\": \"Login successful\"}");
    response.getWriter().flush();
  }

  @Override
  protected void unsuccessfulAuthentication(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
      throws IOException {
    response.setContentType("application/json");
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.getWriter().write("{\"status\": \"error\", \"message\": \"Login failed\"}");
    response.getWriter().flush();
  }
}
