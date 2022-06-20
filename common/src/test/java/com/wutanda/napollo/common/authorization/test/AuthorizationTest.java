package com.wutanda.napollo.common.authorization.test;

import com.wutanda.napollo.common.authorization.Authorization;
import com.wutanda.napollo.common.exception.NapolloException;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AuthorizationTest {

  @Test
  public void testInvalidBasicAuthorization() {
    assertThatThrownBy(() -> Authorization.basicAuthorization("ABc123456"))
        .isInstanceOf(NapolloException.class);
  }

  @Test
  public void testInvalidBearerAuthorization() {
    assertThatThrownBy(() -> Authorization.basicAuthorization("ABc123456"))
        .isInstanceOf(NapolloException.class);
  }

  @Test
  public void testValidBasicAuthorization() throws NapolloException {
    final Authorization basicAuthorization = Authorization.basicAuthorization("Basic ABc123456!");
    assertThat(basicAuthorization).isNotNull();
    assertThat(basicAuthorization.getAuthorizationToken()).isEqualTo("ABc123456!");
  }

  @Test
  public void testValidBearerAuthorization() throws NapolloException {
    final Authorization bearerAuthorization =
        Authorization.bearerAuthorization("Bearer ABc123456!");
    assertThat(bearerAuthorization).isNotNull();
    assertThat(bearerAuthorization.getAuthorizationToken()).isEqualTo("ABc123456!");
  }
}
