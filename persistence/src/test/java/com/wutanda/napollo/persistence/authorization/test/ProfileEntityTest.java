package com.wutanda.napollo.persistence.authorization.test;

import com.wutanda.napollo.persistence.authorization.ProfileEntity;
import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class ProfileEntityTest {

  @Test
  public void testValidEntity() {
    final ProfileEntity profileEntity = new ProfileEntity();
    profileEntity.setProfileType("ADMINISTRATOR");
    profileEntity.setDescription("Profile description");
    profileEntity.setName("Profile Name");
    profileEntity.setPermissions(Arrays.asList("CREATE_PROFILE"));
    assertThat(profileEntity).isNotNull();
    assertThat(profileEntity.getProfileType()).isEqualTo("ADMINISTRATOR");
    assertThat(profileEntity.getDescription()).isEqualTo("Profile description");
    assertThat(profileEntity.getName()).isEqualTo("Profile Name");
    assertThat(profileEntity.getTimestamp()).isNotNull();
    assertThat(profileEntity.getId()).isNotNull();
  }
}
