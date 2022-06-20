package com.wutanda.napollo.common.exception.test;

import com.wutanda.napollo.common.exception.ErrorCode;
import com.wutanda.napollo.common.exception.NapolloException;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class NapolloExceptionTest {

  @Test
  public void testValidAuthorizationException() {
    final NapolloException napolloException =
        NapolloException.authorizationError("Authorization error not available");
    assertThat(napolloException).isNotNull();
    assertThat(napolloException.getErrorCode()).isEqualTo(ErrorCode.AUTHORIZATION_ERROR);
    assertThat(napolloException.getErrorDescription())
        .isEqualTo("Authorization error not available");
  }

  @Test
  public void testValidBadRequestException() {
    final NapolloException napolloException =
        NapolloException.badRequest("Bad request error not available");
    assertThat(napolloException).isNotNull();
    assertThat(napolloException.getErrorCode()).isEqualTo(ErrorCode.BAD_REQUEST);
    assertThat(napolloException.getErrorDescription()).isEqualTo("Bad request error not available");
  }

  @Test
  public void testValidCommunicationErrorException() {
    final NapolloException napolloException =
        NapolloException.communicationError("Communication error found");
    assertThat(napolloException).isNotNull();
    assertThat(napolloException.getErrorCode()).isEqualTo(ErrorCode.COMMUNICATION_ERROR);
    assertThat(napolloException.getErrorDescription()).isEqualTo("Communication error found");
  }

  @Test
  public void testValidInternalErrorException() {
    final NapolloException napolloException =
        NapolloException.internalError("Internal error found");
    assertThat(napolloException).isNotNull();
    assertThat(napolloException.getErrorCode()).isEqualTo(ErrorCode.INTERNAL_ERROR);
    assertThat(napolloException.getErrorDescription()).isEqualTo("Internal error found");
  }
}
