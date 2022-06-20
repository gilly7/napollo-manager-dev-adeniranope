package com.wutanda.napollo.persistence.admin;

import com.wutanda.napollo.common.persistence.BaseEntity;
import com.wutanda.napollo.persistence.users.UserEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(schema = "admin", name = "admin_users")
public class AdminEntity extends BaseEntity {

  private String fullName;

  @ManyToOne private UserEntity user;

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public UserEntity getUser() {
    return user;
  }

  public void setUser(UserEntity user) {
    this.user = user;
  }
}
