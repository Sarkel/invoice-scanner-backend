package com.invoicescanner.security;

import com.invoicescanner.error.message.Authorization;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {

  private static final Logger LOGGER = LogManager.getLogger(AuthEntryPoint.class);

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Pre-authenticated entry point called. JWT exception occurred: " + authException.getMessage());
    }
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, Authorization.INVALID_TOKEN);
  }
}
