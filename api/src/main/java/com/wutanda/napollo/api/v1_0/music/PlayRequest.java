package com.wutanda.napollo.api.v1_0.music;

import com.wutanda.napollo.api.v1_0.common.BaseRequest;

public final class PlayRequest extends BaseRequest {

  private String state;
  private String city;
  private String country;

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }
}
