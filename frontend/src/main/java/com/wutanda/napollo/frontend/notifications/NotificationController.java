package com.wutanda.napollo.frontend.notifications;

import com.wutanda.napollo.api.v1_0.notifications.NotificationRequest;
import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.common.transport.AbstractResponse;
import com.wutanda.napollo.managers.notification.NotificationManager;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = {"Notification APIs"})
public class NotificationController {

  @Autowired private NotificationManager notificationManager;

  @Operation(
      tags = {"Notification APIs"},
      summary = "Send a notification by SMS and/or EMAIL")
  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "notification")
  public ResponseEntity<AbstractResponse> sendNotification(
      @RequestBody NotificationRequest notificationRequest) throws NapolloException {
    this.notificationManager.sendNotification(notificationRequest);
    return ResponseEntity.ok().build();
  }
}
