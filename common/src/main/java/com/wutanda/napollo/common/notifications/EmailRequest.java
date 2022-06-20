package com.wutanda.napollo.common.notifications;

import java.io.Serializable;

public final class EmailRequest implements Serializable {

  private String toEmailAddress;
  private String message;
  private String subject;

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getToEmailAddress() {
    return toEmailAddress;
  }

  public void setToEmailAddress(String toEmailAddress) {
    this.toEmailAddress = toEmailAddress;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
