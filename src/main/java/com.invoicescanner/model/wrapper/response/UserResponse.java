package com.invoicescanner.model.wrapper.response;

import com.invoicescanner.model.entity.UserEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResponse {
  private String userId;
  private String firstName;
  private String lastName;
  private String email;
  private String picture;

  public UserResponse(UserEntity user) {
    this.userId = user.getUserId();
    this.firstName = user.getFirstName();
    this.lastName = user.getLastName();
    this.email = user.getEmail();
    this.picture = user.getPicture();
  }
}
