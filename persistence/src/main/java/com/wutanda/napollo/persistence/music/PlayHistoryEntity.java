package com.wutanda.napollo.persistence.music;

import com.wutanda.napollo.common.persistence.BaseEntity;
import com.wutanda.napollo.persistence.accountusers.AccountUserEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema = "music", name = "play_history")
public class PlayHistoryEntity extends BaseEntity {

  @ManyToOne private MediaEntity media;

  @Temporal(TemporalType.TIMESTAMP)
  private Date historyDate;

  @ManyToOne private AccountUserEntity accountUser;

  private Integer plays;

  public MediaEntity getMedia() {
    return media;
  }

  public void setMedia(MediaEntity media) {
    this.media = media;
  }

  public Date getHistoryDate() {
    return historyDate;
  }

  public void setHistoryDate(Date historyDate) {
    this.historyDate = historyDate;
  }

  public AccountUserEntity getAccountUser() {
    return accountUser;
  }

  public void setAccountUser(AccountUserEntity accountUser) {
    this.accountUser = accountUser;
  }

  public Integer getPlays() {
    return plays;
  }

  public void setPlays(Integer plays) {
    this.plays = plays;
  }
}
