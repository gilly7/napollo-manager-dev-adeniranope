package com.wutanda.napollo.managers.authorization.beans;

import com.wutanda.napollo.api.v1_0.authorization.ProfileRequest;
import com.wutanda.napollo.api.v1_0.authorization.common.Profile;
import com.wutanda.napollo.common.authorization.Authorization;
import com.wutanda.napollo.common.authorization.PermissionRegistry;
import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.common.loggers.LogLevel;
import com.wutanda.napollo.common.transport.AbstractResponse;
import com.wutanda.napollo.converters.authorization.ProfileConverter;
import com.wutanda.napollo.managers.authorization.PermissionManager;
import com.wutanda.napollo.managers.authorization.ProfileManager;
import com.wutanda.napollo.managers.loggers.LoggerManager;
import com.wutanda.napollo.persistence.authorization.ProfileEntity;
import com.wutanda.napollo.persistence.authorization.dao.ProfileEntityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class ProfileManagerBean implements ProfileManager {

  @Autowired private ProfileEntityDao profileEntityDao;

  @Autowired private LoggerManager loggerManager;

  @Autowired private PermissionManager permissionManager;

  @Autowired private ProfileConverter profileConverter;

  @Override
  @Transactional
  public AbstractResponse create(Authorization authorization, ProfileRequest request)
      throws NapolloException {
    this.loggerManager.log(
        ProfileManagerBean.class, LogLevel.TRACE, "Creating a new access profile");
    this.permissionManager.validatePermission(
        authorization, Collections.singletonList(PermissionRegistry.CREATE_PROFILE.name()));
    permissionManager.validateAdministratorAccess(authorization);
    this.profileEntityDao
        .findByName(request.getName())
        .orElseThrow(() -> NapolloException.badRequest("Profile with name already registered"));
    final ProfileEntity profileEntity =
        this.profileConverter
            .createEntity(request)
            .orElseThrow(
                () ->
                    NapolloException.badRequest(
                        "Unable to complete this operation. Please try again"));
    profileEntity.setCreatedUser(this.permissionManager.getAuthorizedUser(authorization).getId());
    profileEntity.setStatus(Boolean.TRUE);
    this.profileEntityDao.save(profileEntity);
    this.loggerManager.log(
        ProfileManagerBean.class, LogLevel.TRACE, "Profile registered successfully");
    return AbstractResponse.response("Profile registered successfully", Boolean.TRUE);
  }

  @Override
  public AbstractResponse<Profile> get(Authorization authorization, String identity)
      throws NapolloException {
    this.loggerManager.log(
        ProfileManagerBean.class,
        LogLevel.TRACE,
        "Getting profile information with id : ".concat(identity));
    this.permissionManager.validatePermission(
        authorization, Collections.singletonList(PermissionRegistry.GET_PROFILE.name()));
    final ProfileEntity persistedProfileEntity =
        this.profileEntityDao
            .findById(identity)
            .orElseThrow(() -> NapolloException.internalError("Profile information not available"));
    final Profile profile =
        this.profileConverter
            .createResponse(persistedProfileEntity)
            .orElseThrow(
                () ->
                    NapolloException.internalError(
                        "Unable to complete this operation. Please contact administrator"));
    return AbstractResponse.response(
        profile, "Profile information fetched successfully", Boolean.TRUE);
  }

  @Override
  public AbstractResponse<Page<Profile>> list(
      Authorization authorization, Integer page, Integer size) throws NapolloException {
    this.loggerManager.log(ProfileManagerBean.class, LogLevel.TRACE, "Listing profile information");
    this.permissionManager.validatePermission(
        authorization, Collections.singletonList(PermissionRegistry.GET_PROFILES.name()));
    permissionManager.validateAdministratorAccess(authorization);
    final Page<ProfileEntity> profileEntityPage =
        this.profileEntityDao.findAll(PageRequest.of(page, size));
    final Page<Profile> profilePage =
        new PageImpl(
            profileEntityPage.stream()
                .filter(ProfileEntity::getStatus)
                .map(
                    profileEntity ->
                        this.profileConverter.createResponse(profileEntity).orElse(null))
                .collect(Collectors.toList()),
            profileEntityPage.getPageable(),
            profileEntityPage.getTotalElements());
    return AbstractResponse.response(profilePage, "Profiles listed successfully", Boolean.TRUE);
  }

  @Override
  @Transactional
  public AbstractResponse update(
      Authorization authorization, String identity, ProfileRequest request)
      throws NapolloException {
    this.loggerManager.log(
        ProfileManagerBean.class, LogLevel.TRACE, "Updating profile information");
    this.permissionManager.validatePermission(
        authorization, Collections.singletonList(PermissionRegistry.UPDATE_PROFILE.name()));
    permissionManager.validateAdministratorAccess(authorization);
    final ProfileEntity persistedProfileEntity =
        this.profileEntityDao
            .findById(identity)
            .orElseThrow(() -> NapolloException.internalError("Profile information not available"));
    if (request.getName() != null) {
      persistedProfileEntity.setName(request.getName());
    }
    if (request.getDescription() != null) {
      persistedProfileEntity.setDescription(request.getDescription());
    }
    if (request.getType() != null) {
      persistedProfileEntity.setProfileType(request.getType());
    }
    this.profileEntityDao.save(persistedProfileEntity);
    return AbstractResponse.response("Profile updated successfully", Boolean.TRUE);
  }

  @Override
  @Transactional
  public AbstractResponse delete(Authorization authorization, String identity)
      throws NapolloException {
    this.loggerManager.log(
        ProfileManagerBean.class, LogLevel.TRACE, "Removing profile information");
    this.permissionManager.validatePermission(
        authorization, Collections.singletonList(PermissionRegistry.DELETE_PROFILE.name()));
    permissionManager.validateAdministratorAccess(authorization);
    final ProfileEntity persistedProfileEntity =
        this.profileEntityDao
            .findById(identity)
            .orElseThrow(() -> NapolloException.internalError("Profile information not available"));
    this.profileEntityDao.delete(persistedProfileEntity);
    return AbstractResponse.response("Profile removed successfully", Boolean.TRUE);
  }

  @Override
  @Transactional
  public AbstractResponse blockProfile(Authorization authorization, String profileIdentity)
      throws NapolloException {
    this.loggerManager.log(ProfileManagerBean.class, LogLevel.TRACE, "Block profile information");
    this.permissionManager.validatePermission(
        authorization, Collections.singletonList(PermissionRegistry.BLOCK_PROFILE.name()));
    permissionManager.validateAdministratorAccess(authorization);
    final ProfileEntity persistedProfileEntity =
        this.profileEntityDao
            .findById(profileIdentity)
            .orElseThrow(() -> NapolloException.internalError("Profile information not available"));
    persistedProfileEntity.setStatus(Boolean.FALSE);
    this.profileEntityDao.save(persistedProfileEntity);
    return AbstractResponse.response("Profile blocked successfully", Boolean.TRUE);
  }

  @Override
  @Transactional
  public AbstractResponse unblockProfile(Authorization authorization, String profileIdentity)
      throws NapolloException {
    this.loggerManager.log(ProfileManagerBean.class, LogLevel.TRACE, "Unblock profile information");
    this.permissionManager.validatePermission(
        authorization, Collections.singletonList(PermissionRegistry.UNBLOCK_PROFILE.name()));
    permissionManager.validateAdministratorAccess(authorization);
    final ProfileEntity persistedProfileEntity =
        this.profileEntityDao
            .findById(profileIdentity)
            .orElseThrow(() -> NapolloException.internalError("Profile information not available"));
    persistedProfileEntity.setStatus(Boolean.TRUE);
    this.profileEntityDao.save(persistedProfileEntity);
    return AbstractResponse.response("Profile unblocked successfully", Boolean.TRUE);
  }
}
