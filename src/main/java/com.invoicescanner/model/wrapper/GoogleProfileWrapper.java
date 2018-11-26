package com.invoicescanner.model.wrapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleProfileWrapper {

  @JsonProperty("sub")
  private String id;
  @JsonProperty("given_name")
  private String firstName;
  @JsonProperty("family_name")
  private String lastName;
  private String email;
  private String picture;

}
