package com.wutanda.napollo.api.v1_0.authorization;

import com.wutanda.napollo.api.v1_0.common.BaseRequest;

import java.io.Serializable;
import java.util.List;

public final class PermissionRequest extends BaseRequest implements Serializable {

  private List<String> permissions;

  public List<String> getPermissions() {
    return permissions;
  }

  public void setPermissions(List<String> permissions) {
    this.permissions = permissions;
  }
}
