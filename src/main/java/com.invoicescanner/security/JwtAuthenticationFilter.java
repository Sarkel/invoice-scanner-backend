package com.invoicescanner.security;

import io.jsonwebtoken.JwtException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final Logger LOGGER = LogManager.getLogger(JwtAuthenticationFilter.class);

  private final JwtTokenUtils tokenUtils;
  private final UserDetailsService userDetailsService;

  public JwtAuthenticationFilter(JwtTokenUtils tokenUtils, UserDetailsService userDetailsService) {
    this.tokenUtils = tokenUtils;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {

    String header = request.getHeader("Authorization");
    if (StringUtils.isNotBlank(header) && header.startsWith("Bearer ")) {
      String token = header.substring(7);

      try {
        String id = tokenUtils.getUserIdFromToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(id);
        if (Objects.nonNull(userDetails) && !tokenUtils.isTokenExpired(token)) {
          UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
          );
          authentication.setDetails(userDetails);
          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      } catch(JwtException e) {
        LOGGER.warn(e.getMessage());
      }
    }
    filterChain.doFilter(request, response);
  }
}
