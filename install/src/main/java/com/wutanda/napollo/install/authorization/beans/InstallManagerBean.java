package com.wutanda.napollo.install.authorization.beans;

import com.google.common.collect.ImmutableList;
import com.wutanda.napollo.api.v1_0.authorization.ProfileRequest;
import com.wutanda.napollo.common.authorization.PermissionRegistry;
import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.converters.authorization.ProfileConverter;
import com.wutanda.napollo.install.authorization.InstallManager;
import com.wutanda.napollo.persistence.authorization.dao.ProfileEntityDao;
import com.wutanda.napollo.persistence.users.UserEntity;
import com.wutanda.napollo.persistence.users.dao.UserEntityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InstallManagerBean
    implements InstallManager, ApplicationListener<ContextRefreshedEvent> {

  @Autowired private ProfileConverter profileConverter;

  @Autowired private ProfileEntityDao profileEntityDao;

  @Autowired private UserEntityDao userEntityDao;

  @Override
  @Transactional
  public void createDefaultProfiles() {
    ProfileRequest profileRequest = new ProfileRequest();
    profileRequest.setDescription("Default Super Administrator Profile");
    profileRequest.setType("SUPERADMINISTRATOR");
    profileRequest.setName("Super Administrator Profile Level 1");
    profileRequest.setPermissions(
        Arrays.stream(PermissionRegistry.values())
            .map(PermissionRegistry::name)
            .collect(Collectors.toList()));

    ProfileRequest adminProfileRequest = new ProfileRequest();
    adminProfileRequest.setDescription("Default Administrator Profile");
    adminProfileRequest.setType("ADMINISTRATOR");
    adminProfileRequest.setName("Administrator Profile Level 1");
    adminProfileRequest.setPermissions(
        Arrays.stream(PermissionRegistry.values())
            .map(PermissionRegistry::name)
            .collect(Collectors.toList()));

    ProfileRequest artistProfileRequest = new ProfileRequest();
    artistProfileRequest.setDescription("Default Artist Profile");
    artistProfileRequest.setType("ARTIST");
    artistProfileRequest.setName("Artist Profile Level 1");
    artistProfileRequest.setPermissions(
        Arrays.stream(PermissionRegistry.values())
            .map(PermissionRegistry::name)
            .collect(Collectors.toList()));

    ProfileRequest listenerProfileRequest = new ProfileRequest();
    listenerProfileRequest.setDescription("Default Listener Profile");
    listenerProfileRequest.setType("LISTENER");
    listenerProfileRequest.setName("Listener Profile Level 1");
    listenerProfileRequest.setPermissions(
        Arrays.stream(PermissionRegistry.values())
            .map(PermissionRegistry::name)
            .collect(Collectors.toList()));

    final List<ProfileRequest> profileRequests =
        ImmutableList.<ProfileRequest>builder()
            .add(profileRequest)
            .add(adminProfileRequest)
            .add(artistProfileRequest)
            .add(listenerProfileRequest)
            .build();

    this.profileEntityDao.saveAll(
        profileRequests.stream()
            .filter(
                profileReq -> !this.profileEntityDao.findByName(profileReq.getName()).isPresent())
            .map(profileReq -> this.profileConverter.createEntity(profileReq).get())
            .collect(Collectors.toList()));
  }

  @Override
  @Transactional
  public void createSuperAdministrator() throws NapolloException {
    if (!this.userEntityDao.findByEmailAddress("superadmin@napollo.com").isPresent()) {
      final UserEntity userEntity = new UserEntity();
      userEntity.setEmailAddress("superadmin@napollo.com");
      userEntity.setProfile(
          this.profileEntityDao
              .findByName("Super Administrator Profile Level 1")
              .orElseThrow(
                  () ->
                      NapolloException.internalError(
                          "Unable to find the default super administrator profile")));
      userEntity.setPassword("ABc123456!");
      userEntity.setMobileNumber("234NAPOLLO");
      userEntity.setAuthorizationStatus("ACTIVE");
      this.userEntityDao.save(userEntity);
    }
  }

  @Transactional
  public void createWebAdministrator() throws NapolloException {
    if (!this.userEntityDao.findByEmailAddress("webadmin@napollo.com").isPresent()) {
      final UserEntity userEntity = new UserEntity();
      userEntity.setEmailAddress("webadmin@napollo.com");
      userEntity.setProfile(
          this.profileEntityDao
              .findByName("Administrator Profile Level 1")
              .orElseThrow(
                  () ->
                      NapolloException.internalError(
                          "Unable to find the default administrator profile")));
      userEntity.setPassword("ABc123456!");
      userEntity.setMobileNumber("234WEBADMIN");
      userEntity.setAuthorizationStatus("ACTIVE");
      this.userEntityDao.save(userEntity);
    }
  }

  @Override
  public void onApplicationEvent(final ContextRefreshedEvent contextRefreshedEvent) {
    try {
      createDefaultProfiles();
      createSuperAdministrator();
      createWebAdministrator();
    } catch (NapolloException napolloException) {
    }
  }
}
