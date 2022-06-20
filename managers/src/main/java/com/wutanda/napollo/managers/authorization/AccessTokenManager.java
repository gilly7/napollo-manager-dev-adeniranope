package com.wutanda.napollo.managers.authorization;

import com.wutanda.napollo.api.v1_0.authorization.common.AccessToken;
import com.wutanda.napollo.api.v1_0.location.LocationRequest;
import com.wutanda.napollo.common.authorization.Authorization;
import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.common.transport.AbstractResponse;

public interface AccessTokenManager {

  AbstractResponse<AccessToken> createAccessToken(
      final Authorization authorization, final LocationRequest locationRequest)
      throws NapolloException;
}
