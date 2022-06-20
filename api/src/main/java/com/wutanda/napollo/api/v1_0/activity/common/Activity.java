package com.wutanda.napollo.api.v1_0.activity.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wutanda.napollo.api.v1_0.accountusers.common.AccountUser;
import com.wutanda.napollo.api.v1_0.common.BaseRequest;
import com.wutanda.napollo.api.v1_0.music.common.Album;
import com.wutanda.napollo.api.v1_0.music.common.Comment;
import com.wutanda.napollo.api.v1_0.music.common.Media;
import com.wutanda.napollo.api.v1_0.music.common.Playlist;

import java.io.Serializable;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Activity extends BaseRequest implements Serializable {

  private String activityType;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
  private Date activityDateTime;

  private Media media;
  private Comment comment;
  private Album album;
  private Playlist playlist;
  private Comment reply;
  private AccountUser accountUser;
  private AccountUser creatingAccountUser;

  public Media getMedia() {
    return media;
  }

  public void setMedia(Media media) {
    this.media = media;
  }

  public Comment getComment() {
    return comment;
  }

  public void setComment(Comment comment) {
    this.comment = comment;
  }

  public Album getAlbum() {
    return album;
  }

  public void setAlbum(Album album) {
    this.album = album;
  }

  public Playlist getPlaylist() {
    return playlist;
  }

  public void setPlaylist(Playlist playlist) {
    this.playlist = playlist;
  }

  public String getActivityType() {
    return activityType;
  }

  public void setActivityType(String activityType) {
    this.activityType = activityType;
  }

  public Date getActivityDateTime() {
    return activityDateTime;
  }

  public void setActivityDateTime(Date activityDateTime) {
    this.activityDateTime = activityDateTime;
  }

  public AccountUser getAccountUser() {
    return accountUser;
  }

  public void setAccountUser(AccountUser accountUser) {
    this.accountUser = accountUser;
  }

  public AccountUser getCreatingAccountUser() {
    return creatingAccountUser;
  }

  public void setCreatingAccountUser(AccountUser creatingAccountUser) {
    this.creatingAccountUser = creatingAccountUser;
  }

  public Comment getReply() {
    return reply;
  }

  public void setReply(Comment reply) {
    this.reply = reply;
  }
}
