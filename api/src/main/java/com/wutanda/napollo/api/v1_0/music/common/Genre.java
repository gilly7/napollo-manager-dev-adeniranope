package com.wutanda.napollo.api.v1_0.music.common;

import com.wutanda.napollo.api.v1_0.common.BaseRequest;

import java.util.Date;

public final class Genre extends BaseRequest {

  private String name;
  private String description;
  private String id;
  private Date timestamp;

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

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }
}
