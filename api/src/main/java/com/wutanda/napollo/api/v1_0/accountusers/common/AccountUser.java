package com.wutanda.napollo.api.v1_0.accountusers.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wutanda.napollo.api.v1_0.authorization.common.Profile;
import com.wutanda.napollo.api.v1_0.common.BaseRequest;
import com.wutanda.napollo.api.v1_0.location.common.Location;
import com.wutanda.napollo.api.v1_0.music.common.Genre;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public final class AccountUser extends BaseRequest implements Serializable {

  private String firstName;
  private String lastName;

  private String emailAddress;
  private String mobileNumber;
  private String username;

  private String website;
  private String countryCode;

  @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
  private Date dateOfBirth;

  private String accountUserType;

  private Boolean emailActivationStatus;
  private Boolean msisdnActivationStatus;
  private String status;

  private String id;
  private List<Genre> genres;
  private Profile profile;
  private String stageName;

  private String profileUrl;
  private Integer followerCount;
  private Integer followingCount;

  private String state;
  private String country;

  private Location lastKnownLocation;

  public Location getLastKnownLocation() {
    return lastKnownLocation;
  }

  public void setLastKnownLocation(Location lastKnownLocation) {
    this.lastKnownLocation = lastKnownLocation;
  }

  public String getProfileUrl() {
    return profileUrl;
  }

  public void setProfileUrl(String profileUrl) {
    this.profileUrl = profileUrl;
  }

  public Integer getFollowerCount() {
    return followerCount;
  }

  public void setFollowerCount(Integer followerCount) {
    this.followerCount = followerCount;
  }

  public Integer getFollowingCount() {
    return followingCount;
  }

  public void setFollowingCount(Integer followingCount) {
    this.followingCount = followingCount;
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

  public Date getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(Date dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public String getAccountUserType() {
    return accountUserType;
  }

  public void setAccountUserType(String accountUserType) {
    this.accountUserType = accountUserType;
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

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public List<Genre> getGenres() {
    return genres;
  }

  public void setGenres(List<Genre> genres) {
    this.genres = genres;
  }

  public Profile getProfile() {
    return profile;
  }

  public void setProfile(Profile profile) {
    this.profile = profile;
  }

  public String getStageName() {
    return stageName;
  }

  public void setStageName(String stageName) {
    this.stageName = stageName;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }
}
