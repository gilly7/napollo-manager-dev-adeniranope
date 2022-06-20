package com.wutanda.napollo.api.v1_0.music.common;

import com.wutanda.napollo.api.v1_0.accountusers.common.AccountUser;
import com.wutanda.napollo.api.v1_0.common.BaseRequest;

import java.util.Date;
import java.util.List;

public final class Comment extends BaseRequest {

  private String comment;
  private Date timestamp;
  private String id;
  private AccountUser accountUser;
  private Media media;
  private List<Comment> replies;

  public Media getMedia() {
    return media;
  }

  public void setMedia(Media media) {
    this.media = media;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public AccountUser getAccountUser() {
    return accountUser;
  }

  public void setAccountUser(AccountUser accountUser) {
    this.accountUser = accountUser;
  }

  public List<Comment> getReplies() {
    return replies;
  }

  public void setReplies(List<Comment> replies) {
    this.replies = replies;
  }
}
