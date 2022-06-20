package com.wutanda.napollo.persistence.accountusers;

import com.wutanda.napollo.common.persistence.BaseEntity;
import com.wutanda.napollo.persistence.music.GenreEntity;
import com.wutanda.napollo.persistence.music.MediaEntity;
import com.wutanda.napollo.persistence.users.UserEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(schema = "accounts", name = "account_users")
public class AccountUserEntity extends BaseEntity {

  private String firstName;
  private String lastName;
  private String countryCode;
  private String province;
  private String website;

  @Temporal(TemporalType.DATE)
  private Date dateOfBirth;

  @Column(nullable = false)
  private String accountUserType;

  private String stageName;

  @ManyToOne private UserEntity user;

  @ManyToOne private AccountUserUpgradeRequestEntity accountUserUpgradeRequestEntity;

  @ManyToMany
  @JoinTable(schema = "accounts", name = "account_users_genres")
  private List<GenreEntity> genres;

  private Integer followerCount;
  private String profileUrl;
  private Integer followingCount;

  @ManyToMany
  @JoinTable(schema = "accounts", name = "follow_account_users")
  private List<AccountUserEntity> followAccountUsers;

  @ManyToMany
  @JoinTable(schema = "accounts", name = "following_account_users")
  private List<AccountUserEntity> followingAccountUsers;

  @ManyToMany
  @JoinTable(schema = "accounts", name = "discovered_media_account_users")
  private List<MediaEntity> discoveredMedia;

  @ManyToMany
  @JoinTable(schema = "accounts", name = "liked_media_account_users")
  private List<MediaEntity> likedMedia;

  public Integer getFollowerCount() {
    return followerCount;
  }

  public void setFollowerCount(Integer followerCount) {
    this.followerCount = followerCount;
  }

  public String getProfileUrl() {
    return profileUrl;
  }

  public void setProfileUrl(String profileUrl) {
    this.profileUrl = profileUrl;
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

  public String getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  public String getWebsite() {
    return website;
  }

  public void setWebsite(String website) {
    this.website = website;
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

  public String getStageName() {
    return stageName;
  }

  public void setStageName(String stageName) {
    this.stageName = stageName;
  }

  public UserEntity getUser() {
    return user;
  }

  public void setUser(UserEntity user) {
    this.user = user;
  }

  public String getProvince() {
    return province;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public AccountUserUpgradeRequestEntity getAccountUserUpgradeRequestEntity() {
    return accountUserUpgradeRequestEntity;
  }

  public void setAccountUserUpgradeRequestEntity(
      AccountUserUpgradeRequestEntity accountUserUpgradeRequestEntity) {
    this.accountUserUpgradeRequestEntity = accountUserUpgradeRequestEntity;
  }

  public List<GenreEntity> getGenres() {
    return genres;
  }

  public void setGenres(List<GenreEntity> genres) {
    this.genres = genres;
  }

  public List<AccountUserEntity> getFollowAccountUsers() {
    return followAccountUsers;
  }

  public void setFollowAccountUsers(List<AccountUserEntity> followAccountUsers) {
    this.followAccountUsers = followAccountUsers;
  }

  public List<AccountUserEntity> getFollowingAccountUsers() {
    return followingAccountUsers;
  }

  public void setFollowingAccountUsers(List<AccountUserEntity> followingAccountUsers) {
    this.followingAccountUsers = followingAccountUsers;
  }

  public List<MediaEntity> getDiscoveredMedia() {
    return discoveredMedia;
  }

  public void setDiscoveredMedia(List<MediaEntity> discoveredMedia) {
    this.discoveredMedia = discoveredMedia;
  }

  public List<MediaEntity> getLikedMedia() {
    return likedMedia;
  }

  public void setLikedMedia(List<MediaEntity> likedMedia) {
    this.likedMedia = likedMedia;
  }
}
