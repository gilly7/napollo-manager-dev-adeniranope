package com.wutanda.napollo.converters.authorization.beans;

import com.wutanda.napollo.api.v1_0.authorization.ProfileRequest;
import com.wutanda.napollo.api.v1_0.authorization.common.Profile;
import com.wutanda.napollo.converters.authorization.ProfileConverter;
import com.wutanda.napollo.persistence.authorization.ProfileEntity;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProfileConverterBean implements ProfileConverter {

  @Override
  public Optional<ProfileEntity> createEntity(ProfileRequest request) {
    if (request != null) {
      final ProfileEntity profileEntity = new ProfileEntity();
      profileEntity.setProfileType(request.getType());
      profileEntity.setPermissions(request.getPermissions());
      profileEntity.setName(request.getName());
      profileEntity.setDescription(request.getDescription());
      profileEntity.setStatus(Boolean.TRUE);
      return Optional.ofNullable(profileEntity);
    }
    return Optional.empty();
  }

  @Override
  public Optional<Profile> createResponse(ProfileEntity entity) {
    if (entity != null) {
      final Profile profile = new Profile();
      profile.setId(entity.getId());
      profile.setTimestamp(
          LocalDateTime.fromDateFields(entity.getTimestamp()).toString("yyyy-MM-dd HH:mm:ss"));
      profile.setDescription(entity.getDescription());
      profile.setType(entity.getProfileType());
      profile.setName(entity.getName());
      profile.setPermissions(entity.getPermissions());
      profile.setStatus(entity.getStatus());
      return Optional.ofNullable(profile);
    }
    return Optional.empty();
  }
}
