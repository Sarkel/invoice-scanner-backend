package com.invoicescanner.model.wrapper.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseWrapper {
  private String jwtToken;
}
