package com.wutanda.napollo.api.v1_0.accountusers;

import com.wutanda.napollo.api.v1_0.common.BaseRequest;

import java.io.Serializable;

public final class EmailActivationRequest extends BaseRequest implements Serializable {

  private String emailAddress;
  private String activationCode;

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  public String getActivationCode() {
    return activationCode;
  }

  public void setActivationCode(String activationCode) {
    this.activationCode = activationCode;
  }
}
