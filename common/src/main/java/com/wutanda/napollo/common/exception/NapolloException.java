package com.wutanda.napollo.common.exception;

public final class NapolloException extends Exception {

  private final ErrorCode errorCode;
  private final String errorDescription;

  public NapolloException(final ErrorCode errorCode, final String errorDescription) {
    this.errorDescription = errorDescription;
    this.errorCode = errorCode;
  }

  public static NapolloException internalError(final String errorDescription) {
    return new NapolloException(ErrorCode.INTERNAL_ERROR, errorDescription);
  }

  public static NapolloException badRequest(final String errorDescription) {
    return new NapolloException(ErrorCode.BAD_REQUEST, errorDescription);
  }

  public static NapolloException communicationError(final String errorDescription) {
    return new NapolloException(ErrorCode.COMMUNICATION_ERROR, errorDescription);
  }

  public static NapolloException authorizationError(final String errorDescription) {
    return new NapolloException(ErrorCode.AUTHORIZATION_ERROR, errorDescription);
  }

  public String getErrorDescription() {
    return errorDescription;
  }

  public ErrorCode getErrorCode() {
    return errorCode;
  }
}
