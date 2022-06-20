package com.wutanda.napollo.managers.authorization;

import com.wutanda.napollo.api.v1_0.authorization.CredentialRequest;
import com.wutanda.napollo.common.authorization.Authorization;
import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.common.transport.AbstractResponse;

public interface CredentialManager {

  AbstractResponse createCredentialOTP(final Authorization authorization, final String emailAddress)
      throws NapolloException;

  AbstractResponse resetCredentialWithOTP(
      final Authorization authorization, String otp, CredentialRequest credentialRequest)
      throws NapolloException;

  AbstractResponse changeCredential(
      final Authorization authorization, final CredentialRequest credentialRequest)
      throws NapolloException;
}
