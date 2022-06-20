package com.wutanda.napollo.persistence.music;

import com.wutanda.napollo.common.persistence.BaseEntity;
import com.wutanda.napollo.persistence.accountusers.AccountUserEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(schema = "music", name = "music_albums")
public class AlbumEntity extends BaseEntity {

  @Column(nullable = false)
  private String name;

  @Column(columnDefinition = "TEXT")
  private String description;

  private Integer year;

  @Column(unique = true)
  private String url;

  @ManyToOne private AccountUserEntity ownerAccountUser;

  @ManyToMany
  @JoinTable(schema = "music", name = "media_album_music")
  private List<MediaEntity> media;

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

  public AccountUserEntity getOwnerAccountUser() {
    return ownerAccountUser;
  }

  public void setOwnerAccountUser(AccountUserEntity ownerAccountUser) {
    this.ownerAccountUser = ownerAccountUser;
  }

  public List<MediaEntity> getMedia() {
    return media;
  }

  public void setMedia(List<MediaEntity> media) {
    this.media = media;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
