package com.wutanda.napollo.managers.authorization;

import com.wutanda.napollo.api.v1_0.authorization.ProfileRequest;
import com.wutanda.napollo.api.v1_0.authorization.common.Profile;
import com.wutanda.napollo.common.authorization.Authorization;
import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.common.managers.AbstractManager;
import com.wutanda.napollo.common.transport.AbstractResponse;

public interface ProfileManager extends AbstractManager<ProfileRequest, Profile> {

  AbstractResponse blockProfile(final Authorization authorization, final String profileIdentity)
      throws NapolloException;

  AbstractResponse unblockProfile(final Authorization authorization, final String profileIdentity)
      throws NapolloException;
}
