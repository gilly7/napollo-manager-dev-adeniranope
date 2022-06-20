package com.wutanda.napollo.api.v1_0.music;

import com.wutanda.napollo.api.v1_0.common.BaseRequest;

import java.io.Serializable;

public final class AlbumRequest extends BaseRequest implements Serializable {

  private String name;
  private String description;
  private Integer year;

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

  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }
}
