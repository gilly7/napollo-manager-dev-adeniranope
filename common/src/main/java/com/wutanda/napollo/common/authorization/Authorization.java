package com.wutanda.napollo.common.authorization;

import com.wutanda.napollo.common.exception.NapolloException;

public final class Authorization {

  private final String authorizationToken;
  private final AuthorizationType authorizationType;

  public Authorization(final String authorizationToken, final AuthorizationType authorizationType) {
    this.authorizationType = authorizationType;
    this.authorizationToken = authorizationToken;
  }

  public static Authorization basicAuthorization(final String authorizationToken)
      throws NapolloException {
    if (!authorizationToken.startsWith("Basic")) {
      throw NapolloException.badRequest("Invalid authorization token");
    }
    return new Authorization(
        authorizationToken.replace("Basic", "").trim(), AuthorizationType.BASIC);
  }

  public static Authorization bearerAuthorization(final String authorizationToken)
      throws NapolloException {
    if (!authorizationToken.startsWith("Bearer")) {
      throw NapolloException.badRequest("Invalid authorization token");
    }
    return new Authorization(
        authorizationToken.replace("Bearer", "").trim(), AuthorizationType.BEARER);
  }

  public String getAuthorizationToken() {
    return authorizationToken;
  }

  public AuthorizationType getAuthorizationType() {
    return authorizationType;
  }
}
