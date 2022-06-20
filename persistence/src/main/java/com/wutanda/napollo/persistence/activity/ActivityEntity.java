package com.wutanda.napollo.persistence.activity;

import com.wutanda.napollo.common.persistence.BaseEntity;
import com.wutanda.napollo.persistence.users.UserEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(schema = "logs", name = "activities")
public class ActivityEntity extends BaseEntity {

  @Column(nullable = false)
  private String activityType;

  @ManyToOne private UserEntity activityUser;

  @Column(nullable = false)
  private String activityOwnerId;

  @Column(nullable = false)
  private String activityDestinationOwnerId;

  public String getActivityDestinationOwnerId() {
    return activityDestinationOwnerId;
  }

  public void setActivityDestinationOwnerId(String activityDestinationOwnerId) {
    this.activityDestinationOwnerId = activityDestinationOwnerId;
  }

  public String getActivityType() {
    return activityType;
  }

  public void setActivityType(String activityType) {
    this.activityType = activityType;
  }

  public UserEntity getActivityUser() {
    return activityUser;
  }

  public void setActivityUser(UserEntity activityUser) {
    this.activityUser = activityUser;
  }

  public String getActivityOwnerId() {
    return activityOwnerId;
  }

  public void setActivityOwnerId(String activityOwnerId) {
    this.activityOwnerId = activityOwnerId;
  }
}
