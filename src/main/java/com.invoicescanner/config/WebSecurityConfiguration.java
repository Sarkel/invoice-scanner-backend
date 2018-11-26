package com.invoicescanner.config;

import com.invoicescanner.config.path.UserPaths;
import com.invoicescanner.security.AuthAccessDeniedHandler;
import com.invoicescanner.security.AuthEntryPoint;
import com.invoicescanner.security.JwtAuthenticationFilter;
import com.invoicescanner.security.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Order(2)
@RequiredArgsConstructor
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  private final UserDetailsService userDetailsService;
  private final JwtTokenUtils tokenUtils;
  private final AuthEntryPoint entryPoint;
  private final AuthAccessDeniedHandler accessDeniedHandler;

  @Autowired
  public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
    authenticationManagerBuilder.userDetailsService(userDetailsService);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .csrf()
        .disable()
      .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and()
        .authorizeRequests()
          .antMatchers(
            UserPaths.CONTROLLER + UserPaths.CURRENT
          )
            .authenticated()
      .and()
        .addFilterBefore(new JwtAuthenticationFilter(tokenUtils, userDetailsService), UsernamePasswordAuthenticationFilter.class)
          .exceptionHandling()
            .accessDeniedHandler(accessDeniedHandler)
        .and()
          .exceptionHandling()
            .authenticationEntryPoint(entryPoint);
  }

  @Override
  public void configure(WebSecurity web) {
    web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
  }

}
