package com.wutanda.napollo.persistence.music;

import com.wutanda.napollo.common.persistence.BaseEntity;
import com.wutanda.napollo.persistence.accountusers.AccountUserEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(schema = "music", name = "music_media")
public class MediaEntity extends BaseEntity {

  @Column(nullable = false)
  private String name;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(precision = 32, scale = 2)
  private Float duration;

  @Column(precision = 32, scale = 2)
  private Float trailerDuration;

  @ManyToOne private GenreEntity genre;

  @ManyToOne private AlbumEntity album;

  @ElementCollection(targetClass = String.class)
  private List<String> featuredArtists;

  @ManyToMany
  @JoinTable(schema = "music", name = "media_tagged_account_users")
  private List<AccountUserEntity> taggedAccountUsers;

  @Column(unique = true)
  private String trailerUrl;

  @Column(unique = true)
  private String mediaUrl;

  @Column(unique = true)
  private String photoUrl;

  private Integer hits;
  private Integer likes;
  private Integer comments;

  @Column(nullable = false)
  private String status;

  @ManyToOne private AccountUserEntity accountUser;

  private Boolean discovered;

  @Column(unique = true)
  private String shortId;
  @ManyToMany
  @JoinTable(schema = "music", name = "music_media_likes")
  private List<AccountUserEntity> accountUserLikes;
  @ManyToMany
  @JoinTable(schema = "music", name = "music_media_comments")
  private List<CommentEntity> mediaComments;

  public Boolean getDiscovered() {
    return discovered;
  }

  public void setDiscovered(Boolean discovered) {
    this.discovered = discovered;
  }

  public List<CommentEntity> getMediaComments() {
    return mediaComments;
  }

  public void setMediaComments(List<CommentEntity> mediaComments) {
    this.mediaComments = mediaComments;
  }

  public AccountUserEntity getAccountUser() {
    return accountUser;
  }

  public void setAccountUser(AccountUserEntity accountUser) {
    this.accountUser = accountUser;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Integer getHits() {
    return hits;
  }

  public void setHits(Integer hits) {
    this.hits = hits;
  }

  public Integer getLikes() {
    return likes;
  }

  public void setLikes(Integer likes) {
    this.likes = likes;
  }

  public Integer getComments() {
    return comments;
  }

  public void setComments(Integer comments) {
    this.comments = comments;
  }

  public String getTrailerUrl() {
    return trailerUrl;
  }

  public void setTrailerUrl(String trailerUrl) {
    this.trailerUrl = trailerUrl;
  }

  public String getMediaUrl() {
    return mediaUrl;
  }

  public void setMediaUrl(String mediaUrl) {
    this.mediaUrl = mediaUrl;
  }

  public String getPhotoUrl() {
    return photoUrl;
  }

  public void setPhotoUrl(String photoUrl) {
    this.photoUrl = photoUrl;
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

  public Float getDuration() {
    return duration;
  }

  public void setDuration(Float duration) {
    this.duration = duration;
  }

  public GenreEntity getGenre() {
    return genre;
  }

  public void setGenre(GenreEntity genre) {
    this.genre = genre;
  }

  public AlbumEntity getAlbum() {
    return album;
  }

  public void setAlbum(AlbumEntity album) {
    this.album = album;
  }

  public List<String> getFeaturedArtists() {
    return featuredArtists;
  }

  public void setFeaturedArtists(List<String> featuredArtists) {
    this.featuredArtists = featuredArtists;
  }

  public List<AccountUserEntity> getTaggedAccountUsers() {
    return taggedAccountUsers;
  }

  public void setTaggedAccountUsers(List<AccountUserEntity> taggedAccountUsers) {
    this.taggedAccountUsers = taggedAccountUsers;
  }

  public Float getTrailerDuration() {
    return trailerDuration;
  }

  public void setTrailerDuration(Float trailerDuration) {
    this.trailerDuration = trailerDuration;
  }

  public List<AccountUserEntity> getAccountUserLikes() {
    return accountUserLikes;
  }

  public void setAccountUserLikes(List<AccountUserEntity> accountUserLikes) {
    this.accountUserLikes = accountUserLikes;
  }

  public String getShortId() {
    return shortId;
  }

  public void setShortId(String shortId) {
    this.shortId = shortId;
  }
}
