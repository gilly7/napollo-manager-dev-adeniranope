package com.wutanda.napollo.api.v1_0.notifications;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wutanda.napollo.api.v1_0.common.BaseRequest;

import java.io.Serializable;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class NotificationRequest extends BaseRequest implements Serializable {

  private String destination;
  private String type;
  private Map<String, String> parameters;
  private String title;
  private String name;
  private String plainMessage;

  public String getPlainMessage() {
    return plainMessage;
  }

  public void setPlainMessage(String plainMessage) {
    this.plainMessage = plainMessage;
  }

  public String getDestination() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Map<String, String> getParameters() {
    return parameters;
  }

  public void setParameters(Map<String, String> parameters) {
    this.parameters = parameters;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
