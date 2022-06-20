package com.wutanda.napollo.converters.authorization;

import com.wutanda.napollo.api.v1_0.authorization.ProfileRequest;
import com.wutanda.napollo.api.v1_0.authorization.common.Profile;
import com.wutanda.napollo.converters.Converter;
import com.wutanda.napollo.persistence.authorization.ProfileEntity;

public interface ProfileConverter extends Converter<ProfileRequest, Profile, ProfileEntity> {}
