package com.wutanda.napollo.persistence.authorization;

import com.wutanda.napollo.common.persistence.BaseEntity;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(schema = "access", name = "access_profiles")
public class ProfileEntity extends BaseEntity {

  @Column(nullable = false, unique = true)
  private String name;

  private String description;

  @Column(nullable = false)
  private String profileType;

  @ElementCollection(targetClass = String.class)
  private List<String> permissions;

  @Column(nullable = false)
  private Boolean status;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getProfileType() {
    return profileType;
  }

  public void setProfileType(String profileType) {
    this.profileType = profileType;
  }

  public List<String> getPermissions() {
    return permissions;
  }

  public void setPermissions(List<String> permissions) {
    this.permissions = permissions;
  }

  public Boolean getStatus() {
    return status;
  }

  public void setStatus(Boolean status) {
    this.status = status;
  }
}
