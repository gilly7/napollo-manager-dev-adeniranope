package com.wutanda.napollo.api.v1_0.music.common;

import com.wutanda.napollo.api.v1_0.accountusers.common.AccountUser;
import com.wutanda.napollo.api.v1_0.common.BaseRequest;

import java.io.Serializable;
import java.util.Date;

public final class Album extends BaseRequest implements Serializable {

  private String name;
  private String description;
  private Integer year;
  private String id;
  private Date timestamp;
  private AccountUser owner;
  private String url;

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

  public AccountUser getOwner() {
    return owner;
  }

  public void setOwner(AccountUser owner) {
    this.owner = owner;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
