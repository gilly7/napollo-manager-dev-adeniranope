package com.wutanda.napollo.managers.admin;

import com.wutanda.napollo.api.v1_0.admin.AdminRequest;
import com.wutanda.napollo.api.v1_0.admin.common.Admin;
import com.wutanda.napollo.common.authorization.Authorization;
import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.common.managers.AbstractManager;
import com.wutanda.napollo.common.transport.AbstractResponse;

public interface AdminManager extends AbstractManager<AdminRequest, Admin> {

  AbstractResponse block(Authorization authorization, String adminIdentity) throws NapolloException;

  AbstractResponse unblock(Authorization authorization, String adminIdentity)
      throws NapolloException;
}
