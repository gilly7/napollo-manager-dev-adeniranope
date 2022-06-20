package com.wutanda.napollo.common.transport.test;

import com.wutanda.napollo.common.transport.AbstractResponse;
import org.junit.Test;
import org.opentest4j.AssertionFailedError;

import static org.assertj.core.api.Assertions.assertThat;

public class AbstractResponseTest {

  @Test
  public void testValidResponse() {
    final AbstractResponse abstractResponse =
        AbstractResponse.response("Response completed successfully", Boolean.TRUE);
    assertThat(abstractResponse.getResponseDescription())
        .isEqualTo("Response completed successfully");
    assertThat(abstractResponse.getResponseStatus()).isTrue();
  }

  @Test
  public void testValidResponseWithData() {
    final AbstractResponse abstractResponse =
        AbstractResponse.response(
            new AbstractResponseTest.Data(), "Response completed successfully", Boolean.TRUE);
    assertThat(abstractResponse.getResponseDescription())
        .isEqualTo("Response completed successfully");
    assertThat(abstractResponse.getResponseStatus()).isTrue();
    assertThat(abstractResponse.getResponseBody()).isInstanceOf(AbstractResponseTest.Data.class);
  }

  @Test(expected = AssertionFailedError.class)
  public void testInvalidResponse() {
    final AbstractResponse abstractResponse =
        AbstractResponse.response("Response completed successfully", Boolean.TRUE);
    assertThat(abstractResponse.getResponseStatus()).isFalse();
  }

  @Test(expected = AssertionError.class)
  public void testInvalidValidResponseWithData() {
    final AbstractResponse abstractResponse =
        AbstractResponse.response(
            new AbstractResponseTest.Data(), "Response completed successfully", Boolean.TRUE);
    assertThat(abstractResponse.getResponseBody()).isInstanceOf(Exception.class);
  }

  public static final class Data {

    private String dataName;
    private String dataValue;

    public String getDataName() {
      return dataName;
    }

    public void setDataName(String dataName) {
      this.dataName = dataName;
    }

    public String getDataValue() {
      return dataValue;
    }

    public void setDataValue(String dataValue) {
      this.dataValue = dataValue;
    }
  }
}
