package com.wutanda.napollo.frontend.errorhandlers;

import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.common.loggers.LogLevel;
import com.wutanda.napollo.common.transport.AbstractResponse;
import com.wutanda.napollo.managers.loggers.LoggerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class FrontendErrorHandler {

  @Autowired private LoggerManager loggerManager;

  @ExceptionHandler(value = {NapolloException.class})
  public ResponseEntity<AbstractResponse> handleNapolloError(
      final NapolloException napolloException, final WebRequest webRequest)
      throws NapolloException {
    this.loggerManager.log(
        FrontendErrorHandler.class,
        LogLevel.ERROR,
        napolloException.getMessage(),
        napolloException);
    switch (napolloException.getErrorCode()) {
      case AUTHORIZATION_ERROR:
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(AbstractResponse.response(napolloException.getErrorDescription(), Boolean.FALSE));
      case BAD_REQUEST:
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(AbstractResponse.response(napolloException.getErrorDescription(), Boolean.FALSE));
      case COMMUNICATION_ERROR:
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
            .body(AbstractResponse.response(napolloException.getErrorDescription(), Boolean.FALSE));
      default:
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(AbstractResponse.response(napolloException.getErrorDescription(), Boolean.FALSE));
    }
  }

  @ExceptionHandler(
      value = {
        NullPointerException.class,
        IllegalStateException.class,
        DataAccessException.class,
        DataIntegrityViolationException.class
      })
  public ResponseEntity<AbstractResponse> handleNullError(RuntimeException runtimeException)
      throws NapolloException {
    this.loggerManager.log(
        FrontendErrorHandler.class,
        LogLevel.ERROR,
        runtimeException.getMessage(),
        runtimeException);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(
            AbstractResponse.response(
                "An internal error has occurred. Please contact administrator.", Boolean.FALSE));
  }
}
