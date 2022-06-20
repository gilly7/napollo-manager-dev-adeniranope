package com.wutanda.napollo.api.v1_0.accountusers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wutanda.napollo.api.v1_0.common.BaseRequest;

import java.io.Serializable;

public final class AccountUserRequest extends BaseRequest implements Serializable {

  @JsonProperty(required = true)
  private String firstName;

  @JsonProperty(required = true)
  private String lastName;

  @JsonProperty(required = true)
  private String emailAddress;

  @JsonProperty(required = true)
  private String mobileNumber;

  @JsonProperty(required = true)
  private String username;

  @JsonProperty(required = true)
  private String password;

  private String website;
  private String state;

  @JsonProperty(required = true)
  private String countryCode;

  private String dateOfBirth;

  private String stageName;

  public String getStageName() {
    return stageName;
  }

  public void setStageName(String stageName) {
    this.stageName = stageName;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
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

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getWebsite() {
    return website;
  }

  public void setWebsite(String website) {
    this.website = website;
  }

  public String getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  public String getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(String dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }
}
