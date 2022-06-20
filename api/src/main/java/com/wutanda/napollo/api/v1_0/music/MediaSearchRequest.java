package com.wutanda.napollo.api.v1_0.music;

import com.wutanda.napollo.api.v1_0.common.BaseRequest;

public final class MediaSearchRequest extends BaseRequest {

  private String title;
  private String genreName;
  private String albumName;
  private String artistName;
  private String countryName;
  private Integer page;
  private Integer size;
  private Boolean sortByDate;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getGenreName() {
    return genreName;
  }

  public void setGenreName(String genreName) {
    this.genreName = genreName;
  }

  public String getAlbumName() {
    return albumName;
  }

  public void setAlbumName(String albumName) {
    this.albumName = albumName;
  }

  public String getArtistName() {
    return artistName;
  }

  public void setArtistName(String artistName) {
    this.artistName = artistName;
  }

  public Integer getPage() {
    return page;
  }

  public void setPage(Integer page) {
    this.page = page;
  }

  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  public Boolean getSortByDate() {
    return sortByDate;
  }

  public void setSortByDate(Boolean sortByDate) {
    this.sortByDate = sortByDate;
  }

  public String getCountryName() {
    return countryName;
  }

  public void setCountryName(String countryName) {
    this.countryName = countryName;
  }
}
