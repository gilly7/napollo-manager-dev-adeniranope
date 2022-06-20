package com.wutanda.napollo.api.v1_0.music;

import com.wutanda.napollo.api.v1_0.common.BaseRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

public final class MediaRequest extends BaseRequest implements Serializable {

  private String title;
  private String description;
  private List<String> featuredArtists;
  private List<String> taggedAccountUsers;
  private String genreId;
  private String albumId;
  private String releaseType;
  private MultipartFile mediaMultipartFile;
  private MultipartFile trailerMultipartFile;
  private MultipartFile photoMultipartFile;

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

  public List<String> getTaggedAccountUsers() {
    return taggedAccountUsers;
  }

  public void setTaggedAccountUsers(List<String> taggedAccountUsers) {
    this.taggedAccountUsers = taggedAccountUsers;
  }

  public String getGenreId() {
    return genreId;
  }

  public void setGenreId(String genreId) {
    this.genreId = genreId;
  }

  public String getAlbumId() {
    return albumId;
  }

  public void setAlbumId(String albumId) {
    this.albumId = albumId;
  }

  public MultipartFile getMediaMultipartFile() {
    return mediaMultipartFile;
  }

  public void setMediaMultipartFile(MultipartFile mediaMultipartFile) {
    this.mediaMultipartFile = mediaMultipartFile;
  }

  public MultipartFile getTrailerMultipartFile() {
    return trailerMultipartFile;
  }

  public void setTrailerMultipartFile(MultipartFile trailerMultipartFile) {
    this.trailerMultipartFile = trailerMultipartFile;
  }

  public MultipartFile getPhotoMultipartFile() {
    return photoMultipartFile;
  }

  public void setPhotoMultipartFile(MultipartFile photoMultipartFile) {
    this.photoMultipartFile = photoMultipartFile;
  }
}
