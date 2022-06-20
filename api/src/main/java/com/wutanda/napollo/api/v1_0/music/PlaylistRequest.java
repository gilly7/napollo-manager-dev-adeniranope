package com.wutanda.napollo.api.v1_0.music;

import com.wutanda.napollo.api.v1_0.common.BaseRequest;
import org.springframework.web.multipart.MultipartFile;

public final class PlaylistRequest extends BaseRequest {

  private String name;
  private String description;
  private MultipartFile artMultipartFile;
  private Boolean visible;

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

  public MultipartFile getArtMultipartFile() {
    return artMultipartFile;
  }

  public void setArtMultipartFile(MultipartFile artMultipartFile) {
    this.artMultipartFile = artMultipartFile;
  }

  public Boolean getVisible() {
    return visible;
  }

  public void setVisible(Boolean visible) {
    this.visible = visible;
  }
}
