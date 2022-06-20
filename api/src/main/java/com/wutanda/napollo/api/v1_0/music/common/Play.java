package com.wutanda.napollo.api.v1_0.music.common;

import com.wutanda.napollo.api.v1_0.accountusers.common.AccountUser;
import com.wutanda.napollo.api.v1_0.common.BaseRequest;

import java.util.Date;

public final class Play extends BaseRequest {

  private Media media;
  private Date historyDate;
  private String id;
  private Integer plays;
  private AccountUser accountUser;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Media getMedia() {
    return media;
  }

  public void setMedia(Media media) {
    this.media = media;
  }

  public Date getHistoryDate() {
    return historyDate;
  }

  public void setHistoryDate(Date historyDate) {
    this.historyDate = historyDate;
  }

  public Integer getPlays() {
    return plays;
  }

  public void setPlays(Integer plays) {
    this.plays = plays;
  }

  public AccountUser getAccountUser() {
    return accountUser;
  }

  public void setAccountUser(AccountUser accountUser) {
    this.accountUser = accountUser;
  }
}
