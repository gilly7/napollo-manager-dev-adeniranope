package com.wutanda.napollo.managers.authorization;

import com.wutanda.napollo.api.v1_0.authorization.PermissionRequest;
import com.wutanda.napollo.common.authorization.Authorization;
import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.common.transport.AbstractResponse;
import com.wutanda.napollo.persistence.users.UserEntity;

import java.util.List;

public interface PermissionManager {

  void validatePermission(final Authorization authorization, final List<String> permissions)
      throws NapolloException;

  AbstractResponse addProfilePermission(
      final Authorization authorization,
      final String profileIdentity,
      final PermissionRequest permissionRequest)
      throws NapolloException;

  AbstractResponse removeProfilePermission(
      final Authorization authorization,
      final String profileIdentity,
      final PermissionRequest permissionRequest)
      throws NapolloException;

  UserEntity getAuthorizedUser(final Authorization authorization) throws NapolloException;

  Boolean isAdministrator(final Authorization authorization) throws NapolloException;

  Boolean isAccountUser(final Authorization authorization) throws NapolloException;

  void validateAccountUserAccess(final Authorization authorization) throws NapolloException;

  void validateAdministratorAccess(final Authorization authorization) throws NapolloException;
}
