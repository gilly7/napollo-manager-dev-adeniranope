package com.wutanda.napollo.api.v1_0.authorization;

import java.io.Serializable;

public final class CredentialRequest implements Serializable {

  private String credential;
  private String confirmCredential;
  private String oldCredential;

  public String getCredential() {
    return credential;
  }

  public void setCredential(String credential) {
    this.credential = credential;
  }

  public String getConfirmCredential() {
    return confirmCredential;
  }

  public void setConfirmCredential(String confirmCredential) {
    this.confirmCredential = confirmCredential;
  }

  public String getOldCredential() {
    return oldCredential;
  }

  public void setOldCredential(String oldCredential) {
    this.oldCredential = oldCredential;
  }
}
