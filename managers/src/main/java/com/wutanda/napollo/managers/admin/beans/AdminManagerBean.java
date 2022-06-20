package com.wutanda.napollo.managers.admin.beans;

import com.google.common.collect.ImmutableMap;
import com.wutanda.napollo.api.v1_0.admin.AdminRequest;
import com.wutanda.napollo.api.v1_0.admin.common.Admin;
import com.wutanda.napollo.api.v1_0.notifications.NotificationRequest;
import com.wutanda.napollo.common.authorization.Authorization;
import com.wutanda.napollo.common.authorization.PermissionRegistry;
import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.common.loggers.LogLevel;
import com.wutanda.napollo.common.transport.AbstractResponse;
import com.wutanda.napollo.converters.admin.AdminConverter;
import com.wutanda.napollo.managers.admin.AdminManager;
import com.wutanda.napollo.managers.authorization.PermissionManager;
import com.wutanda.napollo.managers.loggers.LoggerManager;
import com.wutanda.napollo.managers.notification.NotificationManager;
import com.wutanda.napollo.persistence.admin.AdminEntity;
import com.wutanda.napollo.persistence.admin.dao.AdminEntityDao;
import com.wutanda.napollo.persistence.authorization.ProfileEntity;
import com.wutanda.napollo.persistence.authorization.dao.ProfileEntityDao;
import com.wutanda.napollo.persistence.users.UserEntity;
import com.wutanda.napollo.persistence.users.dao.UserEntityDao;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class AdminManagerBean implements AdminManager {

  @Autowired private ProfileEntityDao profileEntityDao;

  @Autowired private UserEntityDao userEntityDao;

  @Autowired private AdminEntityDao adminEntityDao;

  @Autowired private AdminConverter adminConverter;

  @Autowired private LoggerManager loggerManager;

  @Autowired private PermissionManager permissionManager;

  @Autowired private NotificationManager notificationManager;

  @Override
  @Transactional
  public AbstractResponse create(Authorization authorization, AdminRequest request)
      throws NapolloException {
    this.loggerManager.log(AdminManagerBean.class, LogLevel.TRACE, "Creating a new admin account");
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.CREATE_ADMINISTRATOR.name()));
    if (this.userEntityDao.findByEmailAddress(request.getEmailAddress()).isPresent()) {
      throw NapolloException.internalError("Email address already registered to another user");
    }
    if (this.userEntityDao.findByUsername(request.getUsername()).isPresent()) {
      throw NapolloException.internalError("Username already assigned to another user");
    }

    final ProfileEntity profileEntity =
        this.profileEntityDao
            .findById(request.getProfileId())
            .orElseThrow(() -> NapolloException.badRequest("Profile information not available"));
    if (!Arrays.asList("ADMINISTRATOR", "SUPER_ADMINISTRATOR")
        .contains(profileEntity.getProfileType())) {
      throw NapolloException.internalError(
          "Only administrator and super administrator profiles are allowed");
    }

    final UserEntity userEntity = new UserEntity();
    userEntity.setUsername(request.getUsername());
    userEntity.setPassword(RandomStringUtils.randomAlphanumeric(10));
    userEntity.setEmailAddress(request.getEmailAddress());
    userEntity.setProfile(profileEntity);
    userEntity.setAuthorizationStatus("CREATED");
    userEntity.setEmailActivationToken(RandomStringUtils.randomAlphanumeric(16));
    this.userEntityDao.save(userEntity);

    final AdminEntity adminEntity = new AdminEntity();
    adminEntity.setUser(userEntity);
    adminEntity.setFullName(request.getFullName());
    adminEntity.setCreatedUser(this.permissionManager.getAuthorizedUser(authorization).getId());
    this.adminEntityDao.save(adminEntity);

    final NotificationRequest notificationRequest = new NotificationRequest();
    notificationRequest.setType("EMAIL");
    notificationRequest.setDestination(request.getEmailAddress());
    notificationRequest.setTitle("Your admin account created");
    notificationRequest.setName("createAdmin");
    notificationRequest.setParameters(
        ImmutableMap.<String, String>builder()
            .put("password", userEntity.getPassword())
            .put("token", userEntity.getEmailActivationToken())
            .build());
    this.notificationManager.sendNotification(notificationRequest);

    return AbstractResponse.response("Admin account created successfully", Boolean.TRUE);
  }

  @Override
  public AbstractResponse<Admin> get(Authorization authorization, String identity)
      throws NapolloException {
    this.loggerManager.log(
        AdminManagerBean.class,
        LogLevel.TRACE,
        "Get administrator account with ID : ".concat(identity));
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.GET_ADMINISTRATOR.name()));
    final AdminEntity adminEntity =
        this.adminEntityDao
            .findById(identity)
            .orElseThrow(() -> NapolloException.badRequest("Cannot find admin information"));
    return AbstractResponse.response(
        this.adminConverter
            .createResponse(adminEntity)
            .orElseThrow(() -> NapolloException.internalError("Unable to complete this operation")),
        "Administrator information fetched successfully",
        Boolean.TRUE);
  }

  @Override
  public AbstractResponse<Page<Admin>> list(Authorization authorization, Integer page, Integer size)
      throws NapolloException {
    this.loggerManager.log(
        AdminManagerBean.class, LogLevel.TRACE, "Get a list of all administrators");
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.GET_ADMINISTRATORS.name()));
    final Page<AdminEntity> adminEntityPage =
        this.adminEntityDao.findAll(PageRequest.of(page, size));
    final Page<Admin> adminPage =
        new PageImpl(
            adminEntityPage.stream()
                .map(adminEntity -> this.adminConverter.createResponse(adminEntity).get())
                .collect(Collectors.toList()),
            adminEntityPage.getPageable(),
            adminEntityPage.getTotalElements());
    return AbstractResponse.response(adminPage, "Admins fetched successfully", Boolean.TRUE);
  }

  @Override
  @Transactional
  public AbstractResponse update(Authorization authorization, String identity, AdminRequest request)
      throws NapolloException {
    this.loggerManager.log(
        AdminManagerBean.class,
        LogLevel.TRACE,
        "Get administrator account with ID : ".concat(identity));
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.UPDATE_ADMINISTRATOR.name()));
    final AdminEntity adminEntity =
        this.adminEntityDao
            .findById(identity)
            .orElseThrow(() -> NapolloException.badRequest("Cannot find admin information"));
    if (request.getFullName() != null) {
      adminEntity.setFullName(request.getFullName());
    }
    if (request.getProfileId() != null) {
      final ProfileEntity profileEntity =
          this.profileEntityDao
              .findById(request.getProfileId())
              .orElseThrow(
                  () -> NapolloException.internalError("Profile information not available"));
      if (!Arrays.asList("ADMINISTRATOR", "SUPER_ADMINISTRATOR")
          .contains(adminEntity.getUser().getProfile().getProfileType())) {
        throw NapolloException.internalError(
            "Only administrator and super administrator profiles are allowed");
      }
      final UserEntity userEntity = adminEntity.getUser();
      userEntity.setProfile(profileEntity);
      this.userEntityDao.save(userEntity);
    }
    this.adminEntityDao.save(adminEntity);
    return AbstractResponse.response("Admin updated successfully", Boolean.TRUE);
  }

  @Override
  @Transactional
  public AbstractResponse delete(Authorization authorization, String identity)
      throws NapolloException {
    this.loggerManager.log(
        AdminManagerBean.class,
        LogLevel.TRACE,
        "Remove administrator account with ID : ".concat(identity));
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.CLOSE_ADMINISTRATOR.name()));
    final AdminEntity adminEntity =
        this.adminEntityDao
            .findById(identity)
            .orElseThrow(() -> NapolloException.badRequest("Cannot find admin information"));
    this.adminEntityDao.delete(adminEntity);
    return AbstractResponse.response(
        "Administrator information removed successfully", Boolean.TRUE);
  }

  @Override
  @Transactional
  public AbstractResponse block(Authorization authorization, String adminIdentity)
      throws NapolloException {
    this.loggerManager.log(
        AdminManagerBean.class,
        LogLevel.TRACE,
        "Blocking administrator account with ID : ".concat(adminIdentity));
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.BLOCK_ADMINISTRATOR.name()));
    final AdminEntity adminEntity =
        this.adminEntityDao
            .findById(adminIdentity)
            .orElseThrow(() -> NapolloException.badRequest("Cannot find admin information"));
    final UserEntity userEntity = adminEntity.getUser();
    userEntity.setAuthorizationStatus("BLOCKED");
    this.userEntityDao.save(userEntity);
    final NotificationRequest notificationRequest = new NotificationRequest();
    notificationRequest.setType("EMAIL");
    notificationRequest.setDestination(userEntity.getEmailAddress());
    notificationRequest.setTitle("Your admin account is blocked");
    notificationRequest.setName("blockAdmin");
    notificationRequest.setParameters(ImmutableMap.<String, String>builder().build());
    this.notificationManager.sendNotification(notificationRequest);
    return AbstractResponse.response("Admin account blocked successfully", Boolean.TRUE);
  }

  @Override
  @Transactional
  public AbstractResponse unblock(Authorization authorization, String adminIdentity)
      throws NapolloException {
    this.loggerManager.log(
        AdminManagerBean.class,
        LogLevel.TRACE,
        "Unblocking administrator account with ID : ".concat(adminIdentity));
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.UNBLOCK_ADMINISTRATOR.name()));
    final AdminEntity adminEntity =
        this.adminEntityDao
            .findById(adminIdentity)
            .orElseThrow(() -> NapolloException.badRequest("Cannot find admin information"));
    final UserEntity userEntity = adminEntity.getUser();
    userEntity.setAuthorizationStatus("ACTIVE");
    this.userEntityDao.save(userEntity);
    final NotificationRequest notificationRequest = new NotificationRequest();
    notificationRequest.setType("EMAIL");
    notificationRequest.setDestination(userEntity.getEmailAddress());
    notificationRequest.setTitle("Your admin account is unblocked");
    notificationRequest.setName("unblockAdmin");
    notificationRequest.setParameters(ImmutableMap.<String, String>builder().build());
    this.notificationManager.sendNotification(notificationRequest);
    return AbstractResponse.response("Admin account unblocked successfully", Boolean.TRUE);
  }
}
