package com.invoicescanner.security;

import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class SocialAuthenticationProvider implements AuthenticationProvider {

  private final UserInfoTokenServices userInfoTokenService;

  public SocialAuthenticationProvider(UserInfoTokenServices userInfoTokenService) {
    this.userInfoTokenService = userInfoTokenService;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    SocialAuthentication token = (SocialAuthentication) authentication;
    return userInfoTokenService.loadAuthentication(token.getToken());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return SocialAuthentication.class.isAssignableFrom(authentication);
  }

}
