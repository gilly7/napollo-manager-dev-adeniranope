package com.wutanda.napollo.common.transport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.joda.time.LocalDateTime;

import java.io.Serializable;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class AbstractResponse<T> implements Serializable {

  private final T responseBody;
  private final Boolean responseStatus;
  private final String responseDescription;
  private Date responseTimestamp;

  public AbstractResponse(
      final T responseBody, final Boolean responseStatus, final String responseDescription) {
    this.responseBody = responseBody;
    this.responseDescription = responseDescription;
    this.responseStatus = responseStatus;
    this.responseTimestamp = LocalDateTime.now().toDate();
  }

  public static <T> AbstractResponse response(
      final T responseBody, final String responseDescription, final Boolean responseStatus) {
    return new AbstractResponse(responseBody, responseStatus, responseDescription);
  }

  public static AbstractResponse response(
      final String responseDescription, final Boolean responseStatus) {
    return new AbstractResponse(null, responseStatus, responseDescription);
  }

  public Boolean getResponseStatus() {
    return responseStatus;
  }

  public Date getResponseTimestamp() {
    return responseTimestamp;
  }

  public String getResponseDescription() {
    return responseDescription;
  }

  public T getResponseBody() {
    return responseBody;
  }

  @Override
  public String toString() {
    return Json.toJson(this);
  }
}
