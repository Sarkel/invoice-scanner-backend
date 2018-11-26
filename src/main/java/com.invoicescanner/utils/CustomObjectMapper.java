package com.invoicescanner.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class CustomObjectMapper {

  private final ObjectMapper objectMapper;

  public CustomObjectMapper() {
    objectMapper = new ObjectMapper();
  }

  public <OriginType, ReturnedType> ReturnedType convert(OriginType object, Class<ReturnedType> returnedClass) {
    return objectMapper.convertValue(object, returnedClass);
  }

  @PostConstruct
  public void setupMapper() {
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }
}
