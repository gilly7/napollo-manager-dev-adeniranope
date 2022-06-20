package com.wutanda.napollo.persistence.users;

import com.wutanda.napollo.common.persistence.BaseEntity;
import com.wutanda.napollo.common.persistence.PersistenceDataEncryption;
import com.wutanda.napollo.persistence.authorization.ProfileEntity;
import com.wutanda.napollo.persistence.location.LocationEntity;

import javax.persistence.*;

@Entity
@Table(schema = "users", name = "users")
public class UserEntity extends BaseEntity {

  @Column(unique = true, nullable = false)
  private String emailAddress;

  @Column(unique = true)
  private String mobileNumber;

  @Column(unique = true)
  private String username;

  @Convert(converter = PersistenceDataEncryption.class)
  private String password;

  @Convert(converter = PersistenceDataEncryption.class)
  @Column(unique = true)
  private String passwordToken;

  @Column(unique = true)
  @Convert(converter = PersistenceDataEncryption.class)
  private String msisdnActivationToken;

  @Column(unique = true)
  @Convert(converter = PersistenceDataEncryption.class)
  private String emailActivationToken;

  private Boolean emailActivationStatus;

  private Boolean msisdnActivationStatus;

  @Column(nullable = false)
  private String authorizationStatus;

  @ManyToOne private ProfileEntity profile;

  @ManyToOne private LocationEntity lastKnownLocation;

  public String getAuthorizationStatus() {
    return authorizationStatus;
  }

  public void setAuthorizationStatus(String authorizationStatus) {
    this.authorizationStatus = authorizationStatus;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public String getMobileNumber() {
    return mobileNumber;
  }

  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getMsisdnActivationToken() {
    return msisdnActivationToken;
  }

  public void setMsisdnActivationToken(String msisdnActivationToken) {
    this.msisdnActivationToken = msisdnActivationToken;
  }

  public String getEmailActivationToken() {
    return emailActivationToken;
  }

  public void setEmailActivationToken(String emailActivationToken) {
    this.emailActivationToken = emailActivationToken;
  }

  public ProfileEntity getProfile() {
    return profile;
  }

  public void setProfile(ProfileEntity profile) {
    this.profile = profile;
  }

  public String getPasswordToken() {
    return passwordToken;
  }

  public void setPasswordToken(String passwordToken) {
    this.passwordToken = passwordToken;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Boolean getEmailActivationStatus() {
    return emailActivationStatus;
  }

  public void setEmailActivationStatus(Boolean emailActivationStatus) {
    this.emailActivationStatus = emailActivationStatus;
  }

  public Boolean getMsisdnActivationStatus() {
    return msisdnActivationStatus;
  }

  public void setMsisdnActivationStatus(Boolean msisdnActivationStatus) {
    this.msisdnActivationStatus = msisdnActivationStatus;
  }

  public LocationEntity getLastKnownLocation() {
    return lastKnownLocation;
  }

  public void setLastKnownLocation(LocationEntity lastKnownLocation) {
    this.lastKnownLocation = lastKnownLocation;
  }
}
