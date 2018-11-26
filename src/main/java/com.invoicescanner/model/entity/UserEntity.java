package com.invoicescanner.model.entity;

import com.invoicescanner.model.entity.enums.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class UserEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String userId;
  private String googleId;
  private String firstName;
  private String lastName;
  private String email;
  private String picture;
  private UserRole role;

  public UserEntity(String firstName, String lastName, String email, String picture) {
    this.picture = picture;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.role = UserRole.USER;
  }

}
