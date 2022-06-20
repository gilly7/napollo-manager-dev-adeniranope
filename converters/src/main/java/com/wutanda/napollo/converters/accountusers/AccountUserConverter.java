package com.wutanda.napollo.converters.accountusers;

import com.wutanda.napollo.api.v1_0.accountusers.AccountUserRequest;
import com.wutanda.napollo.api.v1_0.accountusers.common.AccountUser;
import com.wutanda.napollo.converters.Converter;
import com.wutanda.napollo.persistence.accountusers.AccountUserEntity;

public interface AccountUserConverter
    extends Converter<AccountUserRequest, AccountUser, AccountUserEntity> {}
