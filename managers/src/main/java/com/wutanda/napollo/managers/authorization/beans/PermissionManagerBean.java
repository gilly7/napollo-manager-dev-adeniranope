package com.wutanda.napollo.managers.authorization.beans;

import com.wutanda.napollo.api.v1_0.authorization.PermissionRequest;
import com.wutanda.napollo.common.authorization.Authorization;
import com.wutanda.napollo.common.authorization.PermissionRegistry;
import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.common.loggers.LogLevel;
import com.wutanda.napollo.common.transport.AbstractResponse;
import com.wutanda.napollo.managers.authorization.PermissionManager;
import com.wutanda.napollo.managers.loggers.LoggerManager;
import com.wutanda.napollo.persistence.authorization.ProfileEntity;
import com.wutanda.napollo.persistence.authorization.dao.ProfileEntityDao;
import com.wutanda.napollo.persistence.users.UserEntity;
import com.wutanda.napollo.persistence.users.dao.UserEntityDao;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PermissionManagerBean implements PermissionManager {

  private final JwtParser jwtParser = Jwts.parser();
  @Autowired private ProfileEntityDao profileEntityDao;
  @Autowired private UserEntityDao userEntityDao;
  @Autowired private LoggerManager loggerManager;
  @Value("${napollo.authorization.key}")
  private String jwtTokenAuthorizationKey;

  @Override
  public void validatePermission(final Authorization authorization, final List<String> permissions)
      throws NapolloException {
    this.loggerManager.log(
        PermissionManagerBean.class,
        LogLevel.TRACE,
        "Validating permission with token ".concat(permissions.toString()));
    if (!getAuthorizedUser(authorization).getProfile().getPermissions().containsAll(permissions)) {
      throw NapolloException.authorizationError(
          "Unable to verify your access to this operation. Please try again");
    }
  }

  @Override
  @Transactional
  public AbstractResponse addProfilePermission(
      final Authorization authorization,
      final String profileIdentity,
      final PermissionRequest permissionRequest)
      throws NapolloException {
    this.validatePermission(
        authorization, Collections.singletonList(PermissionRegistry.ADD_PROFILE_PERMISSION.name()));
    final ProfileEntity persistedProfileEntity =
        this.profileEntityDao
            .findById(profileIdentity)
            .orElseThrow(() -> NapolloException.badRequest("Profile information not available"));
    persistedProfileEntity
        .getPermissions()
        .addAll(
            permissionRequest.getPermissions().stream()
                .filter(permission -> !persistedProfileEntity.getPermissions().contains(permission))
                .collect(Collectors.toList()));
    this.profileEntityDao.save(persistedProfileEntity);
    return AbstractResponse.response("Permission(s) registered successfully", Boolean.TRUE);
  }

  @Override
  @Transactional
  public AbstractResponse removeProfilePermission(
      final Authorization authorization,
      final String profileIdentity,
      final PermissionRequest permissionRequest)
      throws NapolloException {
    this.validatePermission(
        authorization,
        Collections.singletonList(PermissionRegistry.REMOVE_PROFILE_PERMISSION.name()));
    final ProfileEntity persistedProfileEntity =
        this.profileEntityDao
            .findById(profileIdentity)
            .orElseThrow(() -> NapolloException.badRequest("Profile information not available"));
    persistedProfileEntity
        .getPermissions()
        .removeAll(
            permissionRequest.getPermissions().stream()
                .filter(permission -> persistedProfileEntity.getPermissions().contains(permission))
                .collect(Collectors.toList()));
    this.profileEntityDao.save(persistedProfileEntity);
    return AbstractResponse.response("Permission(s) registered successfully", Boolean.TRUE);
  }

  @Override
  public UserEntity getAuthorizedUser(Authorization authorization) throws NapolloException {
    try {
      jwtParser.setSigningKey(jwtTokenAuthorizationKey);
      final Jws<Claims> claimsJws = jwtParser.parseClaimsJws(authorization.getAuthorizationToken());
      final String userIdentity = claimsJws.getBody().get("userIdentity", String.class);
      final UserEntity authorizedUserEntity =
          this.userEntityDao
              .findById(userIdentity)
              .orElseThrow(
                  () ->
                      NapolloException.authorizationError(
                          "Unable to authorize your access token. Please request a fresh access  token"));
      if (!authorizedUserEntity.getAuthorizationStatus().equals("ACTIVE")) {
        throw NapolloException.internalError(
            "Your account is not active. Please activate your account with the token sent to your email address");
      }
      return authorizedUserEntity;
    } catch (JwtException jwtException) {
      throw NapolloException.authorizationError(
          "Invalid and/or expired access token. Please request a new access token");
    }
  }

  @Override
  public Boolean isAdministrator(Authorization authorization) throws NapolloException {
    return Arrays.asList("SUPERADMINISTRATOR", "ADMINISTRATOR")
        .contains(this.getAuthorizedUser(authorization).getProfile().getProfileType());
  }

  @Override
  public Boolean isAccountUser(Authorization authorization) throws NapolloException {
    return Arrays.asList("ARTIST", "LISTENER")
        .contains(this.getAuthorizedUser(authorization).getProfile().getProfileType());
  }

  @Override
  public void validateAccountUserAccess(Authorization authorization) throws NapolloException {
    if (!isAccountUser(authorization)) {
      throw NapolloException.authorizationError(
          "Invalid access. You are not permitted to access this operation");
    }
  }

  @Override
  public void validateAdministratorAccess(Authorization authorization) throws NapolloException {
    if (!isAdministrator(authorization)) {
      throw NapolloException.authorizationError(
          "Invalid access. You are not permitted to access this operation");
    }
  }
}
