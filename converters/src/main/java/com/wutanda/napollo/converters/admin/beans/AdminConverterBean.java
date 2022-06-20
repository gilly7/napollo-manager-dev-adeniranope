package com.wutanda.napollo.converters.admin.beans;

import com.wutanda.napollo.api.v1_0.admin.AdminRequest;
import com.wutanda.napollo.api.v1_0.admin.common.Admin;
import com.wutanda.napollo.converters.admin.AdminConverter;
import com.wutanda.napollo.converters.authorization.ProfileConverter;
import com.wutanda.napollo.persistence.admin.AdminEntity;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AdminConverterBean implements AdminConverter {

  @Autowired private ProfileConverter profileConverter;

  @Override
  public Optional<AdminEntity> createEntity(AdminRequest request) {
    return Optional.empty();
  }

  @Override
  public Optional<Admin> createResponse(AdminEntity entity) {
    if (entity != null) {
      final Admin admin = new Admin();
      admin.setEmailAddress(entity.getUser().getEmailAddress());
      admin.setFullName(entity.getFullName());
      admin.setUsername(entity.getUser().getUsername());
      admin.setTimestamp(
          LocalDateTime.fromDateFields(entity.getTimestamp()).toString("yyyy-MM-dd HH:mm:ss"));
      admin.setId(entity.getId());
      admin.setProfile(
          this.profileConverter.createResponse(entity.getUser().getProfile()).orElse(null));
      return Optional.ofNullable(admin);
    }
    return Optional.empty();
  }
}
