package com.wutanda.napollo.common.exception.test;

import com.wutanda.napollo.common.exception.ErrorCode;
import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class ErrorCodeTest {

  @Test
  public void testValidErrorCodes() {
    assertThat(Arrays.asList(ErrorCode.values()).contains(ErrorCode.INTERNAL_ERROR));
    assertThat(Arrays.asList(ErrorCode.values()).contains(ErrorCode.AUTHORIZATION_ERROR));
    assertThat(Arrays.asList(ErrorCode.values()).contains(ErrorCode.COMMUNICATION_ERROR));
    assertThat(Arrays.asList(ErrorCode.values()).contains(ErrorCode.BAD_REQUEST));
  }
}
