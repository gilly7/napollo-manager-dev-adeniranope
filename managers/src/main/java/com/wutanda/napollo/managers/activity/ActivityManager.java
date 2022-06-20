package com.wutanda.napollo.managers.activity;

import com.wutanda.napollo.api.v1_0.activity.common.Activity;
import com.wutanda.napollo.common.activity.ActivityRequest;
import com.wutanda.napollo.common.authorization.Authorization;
import com.wutanda.napollo.common.exception.NapolloException;
import org.springframework.data.domain.Page;

public interface ActivityManager {

  void postActivity(Authorization authorization, ActivityRequest activityRequest)
      throws NapolloException;

  Page<Activity> getActivities(Authorization authorization, Integer page, Integer size)
      throws NapolloException;
}
