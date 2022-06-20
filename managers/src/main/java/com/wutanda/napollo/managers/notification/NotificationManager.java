package com.wutanda.napollo.managers.notification;

import com.wutanda.napollo.api.v1_0.notifications.NotificationRequest;
import com.wutanda.napollo.common.exception.NapolloException;

public interface NotificationManager {

  void sendNotification(final NotificationRequest notificationRequest) throws NapolloException;
}
