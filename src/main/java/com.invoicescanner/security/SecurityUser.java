package com.invoicescanner.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.invoicescanner.model.entity.enums.UserRole;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class SecurityUser implements UserDetails {

  public static final String ROLE_PREFIX = "ROLE_";

  private final String id;
  private final String email;
  private final Collection<? extends GrantedAuthority> authorities;

  SecurityUser(String id, String email, UserRole userRole) {
    this.id = id;
    this.email = email;
    Collection<SimpleGrantedAuthority> auths = new ArrayList<>();
    auths.add(new SimpleGrantedAuthority(ROLE_PREFIX + userRole));
    this.authorities = auths;
  }

  public String getId() {
    return id;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return null;
  }

  @Override
  public String getUsername() {
    return null;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @JsonIgnore
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

}
