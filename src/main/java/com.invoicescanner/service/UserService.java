package com.invoicescanner.service;

import com.invoicescanner.model.entity.UserEntity;
import com.invoicescanner.model.wrapper.GoogleProfileWrapper;
import com.invoicescanner.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public UserEntity saveGoogleUser(GoogleProfileWrapper googleDetails) {
    UserEntity newUser = new UserEntity(
        googleDetails.getFirstName(),
        googleDetails.getLastName(),
        googleDetails.getEmail(),
        googleDetails.getPicture()
    );
    UserEntity databaseUser = userRepository.findByGoogleId(googleDetails.getId());
    if (Objects.nonNull(databaseUser)) {
      newUser.setId(databaseUser.getId());
      newUser.setUserId(this.getUniqueId());
    }
    newUser.setGoogleId(googleDetails.getId());
    return userRepository.save(newUser);
  }

  public UserEntity getUserEntityById(String userId) {
    return userRepository.findByUserId(userId);
  }

  private String getUniqueId() {
    boolean isUniqueId = false;
    String randomId = null;
    while (!isUniqueId) {
      randomId = String.valueOf(UUID.randomUUID());
      isUniqueId = Objects.isNull(userRepository.findByUserId(randomId));
    }
    return randomId;
  }
}
