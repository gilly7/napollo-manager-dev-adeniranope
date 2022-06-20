package com.wutanda.napollo.persistence.accountusers;

import com.wutanda.napollo.common.persistence.BaseEntity;
import com.wutanda.napollo.persistence.authorization.ProfileEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(schema = "accounts", name = "account_user_upgrades")
public class AccountUserUpgradeRequestEntity extends BaseEntity {

  @ManyToOne private AccountUserEntity accountUser;

  @ManyToOne private ProfileEntity fromProfile;

  @ManyToOne private ProfileEntity toProfile;

  @Column(nullable = false)
  private String accountUserType;

  @Column(nullable = false)
  private Boolean pendingApproval;

  public AccountUserEntity getAccountUser() {
    return accountUser;
  }

  public void setAccountUser(AccountUserEntity accountUser) {
    this.accountUser = accountUser;
  }

  public ProfileEntity getFromProfile() {
    return fromProfile;
  }

  public void setFromProfile(ProfileEntity fromProfile) {
    this.fromProfile = fromProfile;
  }

  public ProfileEntity getToProfile() {
    return toProfile;
  }

  public void setToProfile(ProfileEntity toProfile) {
    this.toProfile = toProfile;
  }

  public Boolean getPendingApproval() {
    return pendingApproval;
  }

  public void setPendingApproval(Boolean pendingApproval) {
    this.pendingApproval = pendingApproval;
  }

  public String getAccountUserType() {
    return accountUserType;
  }

  public void setAccountUserType(String accountUserType) {
    this.accountUserType = accountUserType;
  }
}
