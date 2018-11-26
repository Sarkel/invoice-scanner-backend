package com.invoicescanner.controller;

import com.invoicescanner.config.path.Authorization;
import com.invoicescanner.model.entity.UserEntity;
import com.invoicescanner.model.wrapper.GoogleProfileWrapper;
import com.invoicescanner.security.JwtTokenUtils;
import com.invoicescanner.security.SecurityUserFactory;
import com.invoicescanner.service.UserService;
import com.invoicescanner.utils.CustomObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping(Authorization.CONTROLLER)
@RequiredArgsConstructor
public class AuthController {

  private final UserService userService;
  private final CustomObjectMapper objectMapper;
  private final JwtTokenUtils tokenUtils;

  @RequestMapping(method = RequestMethod.POST, value = Authorization.GOOGLE_LOGIN)
  public ResponseEntity<String> login(Principal principal) {
    Map googleDetails = (Map)((OAuth2Authentication)principal).getUserAuthentication().getDetails();
    GoogleProfileWrapper googleProfileWrapper = objectMapper.convert(googleDetails, GoogleProfileWrapper.class);
    UserEntity userEntity = userService.saveGoogleUser(googleProfileWrapper);
    String token = tokenUtils.generateToken(SecurityUserFactory.create(userEntity));
    return new ResponseEntity<>(token, HttpStatus.OK);
  }

}
