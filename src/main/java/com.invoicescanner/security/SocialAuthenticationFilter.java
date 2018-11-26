package com.invoicescanner.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

public class SocialAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

  private static final Logger LOGGER = LogManager.getLogger(SocialAuthenticationFilter.class);

  public SocialAuthenticationFilter(String path) {
    super(path);
  }

  @Override
  protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
    return true;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException {

    String authToken;

    try {
      authToken = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
    } catch (IOException e) {
      LOGGER.error(e.getMessage());
      return null;
    }

    return getAuthenticationManager().authenticate(new SocialAuthentication(authToken));
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authResult) throws IOException, ServletException {

    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Authentication successful. Updating SecurityContextHolder to contain: "
        + authResult);
    }

    SecurityContextHolder.getContext().setAuthentication(authResult);

    if (eventPublisher != null) {
      eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(authResult, getClass()));
    }
    chain.doFilter(request, response);
  }

  @Override
  public void setAuthenticationManager(AuthenticationManager authenticationManager) {
    super.setAuthenticationManager(authenticationManager);
  }

}
