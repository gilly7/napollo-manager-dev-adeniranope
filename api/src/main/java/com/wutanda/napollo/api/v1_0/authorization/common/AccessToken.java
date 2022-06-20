package com.wutanda.napollo.api.v1_0.authorization.common;

import com.wutanda.napollo.api.v1_0.common.BaseRequest;

import java.io.Serializable;
import java.util.List;

public final class AccessToken extends BaseRequest implements Serializable {

  private String accessToken;
  private Long expires;
  private String accessType;
  private List<String> permissions;

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public Long getExpires() {
    return expires;
  }

  public void setExpires(Long expires) {
    this.expires = expires;
  }

  public String getAccessType() {
    return accessType;
  }

  public void setAccessType(String accessType) {
    this.accessType = accessType;
  }

  public List<String> getPermissions() {
    return permissions;
  }

  public void setPermissions(List<String> permissions) {
    this.permissions = permissions;
  }
}
