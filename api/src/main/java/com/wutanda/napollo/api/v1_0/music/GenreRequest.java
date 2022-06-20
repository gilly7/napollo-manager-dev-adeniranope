package com.wutanda.napollo.api.v1_0.music;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wutanda.napollo.api.v1_0.common.BaseRequest;

import java.io.Serializable;

public final class GenreRequest extends BaseRequest implements Serializable {

  @JsonProperty(required = true)
  private String name;

  @JsonProperty(required = true)
  private String description;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
