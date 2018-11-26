package com.invoicescanner.controller;

import com.invoicescanner.config.path.UserPaths;
import com.invoicescanner.model.entity.UserEntity;
import com.invoicescanner.security.JwtTokenUtils;
import com.invoicescanner.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(UserPaths.CONTROLLER)
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final JwtTokenUtils tokenUtils;

  @RequestMapping(method = RequestMethod.GET, value = UserPaths.CURRENT)
  public ResponseEntity<UserEntity> getCurrentUser(@RequestHeader HttpHeaders headers) {
    String token = headers.get("Authorization").get(0).substring(7);
    String userId = tokenUtils.getUserIdFromToken(token);
    return Optional.ofNullable(userService.getUserEntityById(userId))
      .map(user  -> new ResponseEntity<>(user, HttpStatus.OK))
      .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

}
