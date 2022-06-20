package com.wutanda.napollo.common.transport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public final class Json {

  public static <T> String toJson(final T data) {
    try {
      final ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
      objectMapper.setDefaultPrettyPrinter(new DefaultPrettyPrinter());
      return objectMapper.writeValueAsString(data);
    } catch (JsonProcessingException jsonProcessingException) {
      return null;
    }
  }
}
