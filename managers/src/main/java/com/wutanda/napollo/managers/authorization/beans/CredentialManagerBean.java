package com.wutanda.napollo.managers.authorization.beans;

import com.google.common.collect.ImmutableMap;
import com.wutanda.napollo.api.v1_0.authorization.CredentialRequest;
import com.wutanda.napollo.api.v1_0.notifications.NotificationRequest;
import com.wutanda.napollo.common.authorization.Authorization;
import com.wutanda.napollo.common.authorization.PermissionRegistry;
import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.common.loggers.LogLevel;
import com.wutanda.napollo.common.transport.AbstractResponse;
import com.wutanda.napollo.managers.authorization.CredentialManager;
import com.wutanda.napollo.managers.authorization.PermissionManager;
import com.wutanda.napollo.managers.loggers.LoggerManager;
import com.wutanda.napollo.managers.notification.NotificationManager;
import com.wutanda.napollo.persistence.users.UserEntity;
import com.wutanda.napollo.persistence.users.dao.UserEntityDao;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Component
public class CredentialManagerBean implements CredentialManager {

  @Autowired private PermissionManager permissionManager;

  @Autowired private LoggerManager loggerManager;

  @Autowired private UserEntityDao userEntityDao;

  @Autowired private NotificationManager notificationManager;

  @Override
  @Transactional
  public AbstractResponse createCredentialOTP(Authorization authorization, String emailAddress)
      throws NapolloException {
    this.loggerManager.log(
        CredentialManagerBean.class,
        LogLevel.TRACE,
        "Create credential OTP : ".concat(emailAddress));
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.CREATE_CREDENTIAL_OTP.name()));
    permissionManager.validateAdministratorAccess(authorization);
    final UserEntity persistedUserEntity =
        this.userEntityDao
            .findByEmailAddress(emailAddress)
            .orElseThrow(
                () ->
                    NapolloException.internalError("Unable to create token for credential reset"));
    persistedUserEntity.setPasswordToken(RandomStringUtils.randomNumeric(10));
    this.userEntityDao.save(persistedUserEntity);
    final NotificationRequest notificationRequest = new NotificationRequest();
    notificationRequest.setDestination(persistedUserEntity.getEmailAddress());
    notificationRequest.setTitle("Your password token for a reset");
    notificationRequest.setName("createCredentialOTP");
    notificationRequest.setType("EMAIL");
    notificationRequest.setParameters(
        ImmutableMap.<String, String>builder()
            .put("token", persistedUserEntity.getPasswordToken())
            .build());
    this.notificationManager.sendNotification(notificationRequest);
    return AbstractResponse.response("OTP created successfully", Boolean.TRUE);
  }

  @Override
  @Transactional
  public AbstractResponse resetCredentialWithOTP(
      Authorization authorization, String otp, CredentialRequest credentialRequest)
      throws NapolloException {
    this.loggerManager.log(
        CredentialManagerBean.class, LogLevel.TRACE, "Reset credential with OTP");
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.RESET_CREDENTIAL.name()));
    if (!credentialRequest.getCredential().equals(credentialRequest.getConfirmCredential())) {
      throw NapolloException.internalError("Credential mismatch. Please try again");
    }
    permissionManager.validateAdministratorAccess(authorization);
    final UserEntity persistedUserEntity =
        this.userEntityDao
            .findByPasswordToken(otp)
            .orElseThrow(
                () -> NapolloException.badRequest("Invalid credential token. Please try again"));
    persistedUserEntity.setPasswordToken(null);
    persistedUserEntity.setPassword(credentialRequest.getCredential());
    this.userEntityDao.save(persistedUserEntity);
    final NotificationRequest notificationRequest = new NotificationRequest();
    notificationRequest.setDestination(persistedUserEntity.getEmailAddress());
    notificationRequest.setTitle("Your password reset was successful");
    notificationRequest.setName("resetCredentialWithOTP");
    notificationRequest.setType("EMAIL");
    notificationRequest.setParameters(ImmutableMap.<String, String>builder().build());
    this.notificationManager.sendNotification(notificationRequest);
    return AbstractResponse.response("Password changed successfully", Boolean.TRUE);
  }

  @Override
  @Transactional
  public AbstractResponse changeCredential(
      Authorization authorization, CredentialRequest credentialRequest) throws NapolloException {
    this.loggerManager.log(
        CredentialManagerBean.class, LogLevel.TRACE, "Changing/updating credentials");
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.CHANGE_CREDENTIAL.name()));
    permissionManager.validateAccountUserAccess(authorization);
    final UserEntity authorizedUserEntity = this.permissionManager.getAuthorizedUser(authorization);
    if (!credentialRequest.getCredential().equals(credentialRequest.getConfirmCredential())) {
      throw NapolloException.internalError("Credential mismatch. Please try again");
    }
    if (authorizedUserEntity.getPassword().equals(credentialRequest.getOldCredential())) {
      throw NapolloException.authorizationError(
          "Invalid old password. Please use a valid password");
    }
    authorizedUserEntity.setPassword(credentialRequest.getCredential());
    this.userEntityDao.save(authorizedUserEntity);
    final NotificationRequest notificationRequest = new NotificationRequest();
    notificationRequest.setDestination(authorizedUserEntity.getEmailAddress());
    notificationRequest.setTitle("Your password reset was successful");
    notificationRequest.setName("changeCredential");
    notificationRequest.setType("EMAIL");
    notificationRequest.setParameters(ImmutableMap.<String, String>builder().build());
    this.notificationManager.sendNotification(notificationRequest);
    return AbstractResponse.response("Password changed successfully", Boolean.TRUE);
  }
}
