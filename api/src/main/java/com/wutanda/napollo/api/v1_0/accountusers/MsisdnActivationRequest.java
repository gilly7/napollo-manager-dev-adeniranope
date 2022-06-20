package com.wutanda.napollo.api.v1_0.accountusers;

import com.wutanda.napollo.api.v1_0.common.BaseRequest;

import java.io.Serializable;

public final class MsisdnActivationRequest extends BaseRequest implements Serializable {

  private String mobileNumber;
  private String activationToken;

  public String getMobileNumber() {
    return mobileNumber;
  }

  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }

  public String getActivationToken() {
    return activationToken;
  }

  public void setActivationToken(String activationToken) {
    this.activationToken = activationToken;
  }
}
