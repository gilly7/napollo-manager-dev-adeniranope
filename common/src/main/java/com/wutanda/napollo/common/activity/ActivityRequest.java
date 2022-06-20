package com.wutanda.napollo.common.activity;

import java.io.Serializable;

public final class ActivityRequest implements Serializable {

  private ActivityType activityType;
  private String activitySourceOwnerId;
  private String activityDestinationOwnerId;
  private String activityUserId;

  public String getActivityUserId() {
    return activityUserId;
  }

  public void setActivityUserId(String activityUserId) {
    this.activityUserId = activityUserId;
  }

  public ActivityType getActivityType() {
    return activityType;
  }

  public void setActivityType(ActivityType activityType) {
    this.activityType = activityType;
  }

  public String getActivitySourceOwnerId() {
    return activitySourceOwnerId;
  }

  public void setActivitySourceOwnerId(String activitySourceOwnerId) {
    this.activitySourceOwnerId = activitySourceOwnerId;
  }

  public String getActivityDestinationOwnerId() {
    return activityDestinationOwnerId;
  }

  public void setActivityDestinationOwnerId(String activityDestinationOwnerId) {
    this.activityDestinationOwnerId = activityDestinationOwnerId;
  }
}
