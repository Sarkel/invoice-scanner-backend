package com.invoicescanner.security;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;

@Data
@Getter
public class ClientResources {

  @NestedConfigurationProperty
  private final AuthorizationCodeResourceDetails client = new AuthorizationCodeResourceDetails();

  @NestedConfigurationProperty
  private final ResourceServerProperties resource = new ResourceServerProperties();

}
