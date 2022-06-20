package com.wutanda.napollo.persistence.music;

import com.wutanda.napollo.common.persistence.BaseEntity;
import com.wutanda.napollo.persistence.accountusers.AccountUserEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(schema = "music", name = "media_comments")
public final class CommentEntity extends BaseEntity {

  @ManyToOne private MediaEntity media;

  @Column(columnDefinition = "TEXT")
  private String comment;

  @ManyToOne private AccountUserEntity accountUser;

  @ManyToMany
  @JoinTable(schema = "music", name = "media_comments_replies")
  private List<CommentEntity> replies;

  private Boolean replyState;

  public Boolean getReplyState() {
    return replyState;
  }

  public void setReplyState(Boolean replyState) {
    this.replyState = replyState;
  }

  public MediaEntity getMedia() {
    return media;
  }

  public void setMedia(MediaEntity media) {
    this.media = media;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public AccountUserEntity getAccountUser() {
    return accountUser;
  }

  public void setAccountUser(AccountUserEntity accountUser) {
    this.accountUser = accountUser;
  }

  public List<CommentEntity> getReplies() {
    return replies;
  }

  public void setReplies(List<CommentEntity> replies) {
    this.replies = replies;
  }
}
