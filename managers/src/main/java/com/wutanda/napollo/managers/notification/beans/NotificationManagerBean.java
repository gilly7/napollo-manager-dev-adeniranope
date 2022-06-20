package com.wutanda.napollo.managers.notification.beans;

import com.wutanda.napollo.api.v1_0.notifications.NotificationRequest;
import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.common.notifications.EmailResponse;
import com.wutanda.napollo.managers.notification.NotificationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ArrayBlockingQueue;

@Component
public class NotificationManagerBean implements NotificationManager {

  private static final ArrayBlockingQueue<NotificationRequest> notificationArrayBlockingQueue =
      new ArrayBlockingQueue<>(10000);
  @Autowired private RestTemplate restTemplate;
  @Value("${notification.email.url}")
  private String notificationEmailUrl;
  @Value("${notification.email.key}")
  private String notificationEmailKey;

  @Override
  public void sendNotification(NotificationRequest notificationRequest) throws NapolloException {
    notificationArrayBlockingQueue.add(notificationRequest);
  }

  @Scheduled(fixedDelay = 5000)
  void scheduleNotifications() throws Exception {
    final NotificationRequest notificationRequest = notificationArrayBlockingQueue.take();
    switch (notificationRequest.getType()) {
      case "EMAIL":
        sendEmailNotification(notificationRequest);
        break;
      case "SMS":
        break;
    }
  }

  private void sendEmailNotification(NotificationRequest notificationRequest) {
    final HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setBasicAuth("api", notificationEmailKey);
    httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    final MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
    multiValueMap.add("from", "info@napollomusic.com");
    multiValueMap.add("to", notificationRequest.getDestination());
    multiValueMap.add("subject", notificationRequest.getTitle());
    multiValueMap.add("text", notificationRequest.getPlainMessage());
    final HttpEntity httpEntity = new HttpEntity(multiValueMap, httpHeaders);
    restTemplate.exchange(
        notificationEmailUrl.concat("/messages"),
        HttpMethod.POST,
        httpEntity,
        new ParameterizedTypeReference<EmailResponse>() {});
  }
}
