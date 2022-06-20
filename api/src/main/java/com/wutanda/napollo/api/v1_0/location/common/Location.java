package com.wutanda.napollo.api.v1_0.location.common;

import java.io.Serializable;
import java.math.BigDecimal;

public final class Location implements Serializable {

  private BigDecimal longitude;
  private BigDecimal latitude;
  private String city;
  private String country;

  public BigDecimal getLongitude() {
    return longitude;
  }

  public void setLongitude(BigDecimal longitude) {
    this.longitude = longitude;
  }

  public BigDecimal getLatitude() {
    return latitude;
  }

  public void setLatitude(BigDecimal latitude) {
    this.latitude = latitude;
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
