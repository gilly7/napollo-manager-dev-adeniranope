package com.wutanda.napollo.persistence.music;

import com.wutanda.napollo.common.persistence.BaseEntity;
import com.wutanda.napollo.persistence.accountusers.AccountUserEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(schema = "music", name = "playlists")
public final class PlaylistEntity extends BaseEntity {

  private String name;

  @Column(columnDefinition = "TEXT")
  private String description;

  @ManyToOne private AccountUserEntity accountUser;

  @ManyToMany
  @JoinTable(schema = "music", name = "playlist_music")
  private List<MediaEntity> media;

  @Column(nullable = false)
  private Boolean visible;

  private String url;

  public Boolean getVisible() {
    return visible;
  }

  public void setVisible(Boolean visible) {
    this.visible = visible;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

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

  public AccountUserEntity getAccountUser() {
    return accountUser;
  }

  public void setAccountUser(AccountUserEntity accountUser) {
    this.accountUser = accountUser;
  }

  public List<MediaEntity> getMedia() {
    return media;
  }

  public void setMedia(List<MediaEntity> media) {
    this.media = media;
  }
}
