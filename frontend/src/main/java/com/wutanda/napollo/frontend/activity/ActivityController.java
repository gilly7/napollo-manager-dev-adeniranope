package com.wutanda.napollo.frontend.activity;

import com.wutanda.napollo.api.v1_0.activity.common.Activity;
import com.wutanda.napollo.common.authorization.Authorization;
import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.managers.activity.ActivityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ActivityController {

  @Autowired private ActivityManager activityManager;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "activities")
  public ResponseEntity<Page<Activity>> getActivities(
      @RequestHeader("Authorization") String authorization,
      @RequestParam("page") Integer page,
      @RequestParam("size") Integer size)
      throws NapolloException {
    return ResponseEntity.ok(
        activityManager.getActivities(
            Authorization.bearerAuthorization(authorization), page, size));
  }
}
