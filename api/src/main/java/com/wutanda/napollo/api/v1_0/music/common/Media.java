package com.wutanda.napollo.api.v1_0.music.common;

import com.wutanda.napollo.api.v1_0.accountusers.common.AccountUser;
import com.wutanda.napollo.api.v1_0.common.BaseRequest;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public final class Media extends BaseRequest implements Serializable {

  private String title;
  private String description;
  private List<String> featuredArtists;
  private List<AccountUser> taggedAccountUsers;
  private AccountUser ownerAccountUser;
  private Genre genre;
  private Album album;
  private Float duration;
  private Float trailerDuration;
  private String trailer;
  private String url;
  private String image;
  private Integer hits;
  private Integer likes;
  private Integer comments;
  private String id;
  private Date timestamp;
  private Boolean discovered;
  private String shortId;

  public Boolean getDiscovered() {
    return discovered;
  }

  public void setDiscovered(Boolean discovered) {
    this.discovered = discovered;
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

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<String> getFeaturedArtists() {
    return featuredArtists;
  }

  public void setFeaturedArtists(List<String> featuredArtists) {
    this.featuredArtists = featuredArtists;
  }

  public List<AccountUser> getTaggedAccountUsers() {
    return taggedAccountUsers;
  }

  public void setTaggedAccountUsers(List<AccountUser> taggedAccountUsers) {
    this.taggedAccountUsers = taggedAccountUsers;
  }

  public Genre getGenre() {
    return genre;
  }

  public void setGenre(Genre genre) {
    this.genre = genre;
  }

  public Float getDuration() {
    return duration;
  }

  public void setDuration(Float duration) {
    this.duration = duration;
  }

  public Float getTrailerDuration() {
    return trailerDuration;
  }

  public void setTrailerDuration(Float trailerDuration) {
    this.trailerDuration = trailerDuration;
  }

  public String getTrailer() {
    return trailer;
  }

  public void setTrailer(String trailer) {
    this.trailer = trailer;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
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

  public Album getAlbum() {
    return album;
  }

  public void setAlbum(Album album) {
    this.album = album;
  }

  public AccountUser getOwnerAccountUser() {
    return ownerAccountUser;
  }

  public void setOwnerAccountUser(AccountUser ownerAccountUser) {
    this.ownerAccountUser = ownerAccountUser;
  }

  public String getShortId() {
    return shortId;
  }

  public void setShortId(String shortId) {
    this.shortId = shortId;
  }
}
