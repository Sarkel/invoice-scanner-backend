package com.invoicescanner.security;

import com.invoicescanner.error.message.Authorization;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthAccessDeniedHandler implements AccessDeniedHandler {

  private static final Logger LOGGER = LogManager.getLogger(AuthAccessDeniedHandler.class);

  @Override
  public void handle(
    HttpServletRequest request,
    HttpServletResponse response,
    AccessDeniedException accessDeniedException) throws IOException {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("AccessDeniedHandler called. Access denied: " + accessDeniedException.getMessage());
    }
    response.sendError(HttpServletResponse.SC_FORBIDDEN, Authorization.ACCESS_DENIED);
  }
}
