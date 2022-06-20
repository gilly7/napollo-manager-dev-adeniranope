package com.wutanda.napollo.api.v1_0.common;

import com.wutanda.napollo.common.transport.Json;

public class BaseRequest {

  @Override
  public String toString() {
    return Json.toJson(this);
  }
}
