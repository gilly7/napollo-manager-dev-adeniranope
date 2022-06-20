package com.wutanda.napollo.converters.authorization.beans.test;

import com.wutanda.napollo.api.v1_0.authorization.ProfileRequest;
import com.wutanda.napollo.api.v1_0.authorization.common.Profile;
import com.wutanda.napollo.converters.authorization.ProfileConverter;
import com.wutanda.napollo.converters.authorization.beans.ProfileConverterBean;
import com.wutanda.napollo.persistence.authorization.ProfileEntity;
import org.junit.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class ProfileConverterBeanTest {

  private final ProfileConverter profileConverter = new ProfileConverterBean();

  @Test
  public void convertEntity() {
    final ProfileEntity profileEntity = new ProfileEntity();
    profileEntity.setProfileType("ADMIN");
    profileEntity.setName("Admin Profile");
    profileEntity.setDescription("Admin Profile Description");
    profileEntity.setPermissions(Arrays.asList("CREATE_PROFILE"));
    final Optional<Profile> profileOptionalValue =
        this.profileConverter.createResponse(profileEntity);
    assertThat(profileOptionalValue.isPresent()).isTrue();
    final Profile profile = profileOptionalValue.get();
    assertThat(profile.getName()).isEqualTo("Admin Profile");
    assertThat(profile.getDescription()).isEqualTo("Admin Profile Description");
  }

  @Test
  public void convertRequest() {
    final ProfileRequest profile = new ProfileRequest();
    profile.setName("Name");
    profile.setDescription("Description");
    profile.setPermissions(Arrays.asList("CREATE_PROFILE"));
    profile.setType("Admin");
    final Optional<ProfileEntity> profileEntityOptional =
        this.profileConverter.createEntity(profile);
    assertThat(profileEntityOptional.isPresent()).isTrue();
    final ProfileEntity profileEntity = profileEntityOptional.get();
    assertThat(profileEntity.getName()).isEqualTo("Name");
    assertThat(profileEntity.getDescription()).isEqualTo("Description");
    assertThat(profileEntity.getProfileType()).isEqualTo("Admin");
  }
}
