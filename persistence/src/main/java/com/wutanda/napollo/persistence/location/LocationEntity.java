package com.wutanda.napollo.persistence.location;

import com.wutanda.napollo.common.persistence.BaseEntity;
import com.wutanda.napollo.persistence.users.UserEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(schema = "location", name = "access_locations")
public class LocationEntity extends BaseEntity {

  @Column(precision = 32, scale = 2)
  private BigDecimal longitude;

  @Column(precision = 32, scale = 2)
  private BigDecimal latitude;

  private String city;

  private String country;

  @ManyToOne private UserEntity user;

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

  public UserEntity getUser() {
    return user;
  }

  public void setUser(UserEntity user) {
    this.user = user;
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
