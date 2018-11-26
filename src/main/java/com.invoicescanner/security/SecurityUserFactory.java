package com.invoicescanner.security;

import com.invoicescanner.model.entity.UserEntity;

import java.util.Objects;

public final class SecurityUserFactory {

  private SecurityUserFactory() {}

  public static SecurityUser create(UserEntity user) {
    SecurityUser securityUser = null;
    if (Objects.nonNull(user)) {
      securityUser =  new SecurityUser(user.getUserId(), user.getEmail(), user.getRole());
    }
    return securityUser;
  }

}
