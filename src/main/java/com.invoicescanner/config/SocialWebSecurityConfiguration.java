package com.invoicescanner.config;

import com.invoicescanner.config.path.Authorization;
import com.invoicescanner.security.ClientResources;
import com.invoicescanner.security.SocialAuthenticationFilter;
import com.invoicescanner.security.SocialAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.filter.CompositeFilter;

import javax.servlet.Filter;
import java.util.Collections;
import java.util.List;

@EnableOAuth2Client
@Configuration
@Order(1)
@RequiredArgsConstructor
public class SocialWebSecurityConfiguration extends WebSecurityConfigurerAdapter {
  private static final String SOCIAL_AUTHORIZATION_PATH = Authorization.CONTROLLER + Authorization.GOOGLE_LOGIN;

  private final OAuth2ClientContext oauth2ClientContext;

  @Bean
  public RequestContextListener requestContextListener() {
    return new RequestContextListener();
  }

  @Bean
  @ConfigurationProperties("google")
  public ClientResources googleClientResources() {
    return new ClientResources();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .antMatcher(SOCIAL_AUTHORIZATION_PATH)
      .exceptionHandling()
      .and()
      .csrf()
        .disable()
      .addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class)
      .authorizeRequests()
        .anyRequest()
          .authenticated();
  }

  private Filter ssoFilter() {
    CompositeFilter filter = new CompositeFilter();
    Filter ssoFilter = ssoFilter(googleClientResources(), SOCIAL_AUTHORIZATION_PATH);
    List<Filter> filters = Collections.singletonList(ssoFilter);
    filter.setFilters(filters);
    return filter;
  }

  private Filter ssoFilter(ClientResources client, String path) {
    SocialAuthenticationFilter filter = new SocialAuthenticationFilter(path);
    List<AuthenticationProvider> providers = Collections.singletonList(getProvider(client));
    filter.setAuthenticationManager(new ProviderManager(providers));
    return  filter;
  }

  private AuthenticationProvider getProvider(ClientResources client){
    OAuth2RestTemplate googleTemplate = new OAuth2RestTemplate(client.getClient(), oauth2ClientContext);
    UserInfoTokenServices tokenServices = new UserInfoTokenServices(
          client.getResource().getUserInfoUri(),
          client.getClient().getClientId()
        );
    tokenServices.setRestTemplate(googleTemplate);
    return new SocialAuthenticationProvider(tokenServices);
  }

}
