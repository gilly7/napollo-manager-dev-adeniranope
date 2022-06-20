package com.wutanda.napollo.common.persistence;

import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.LocalDateTime;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(schema = "common", name = "base_entities")
public class BaseEntity implements Serializable {

  @Id private String id;

  @CreatedDate
  @Temporal(TemporalType.TIMESTAMP)
  private Date timestamp;

  @CreatedBy private String createdUser;

  public BaseEntity() {
    this.id = RandomStringUtils.randomAlphanumeric(32);
    this.timestamp = LocalDateTime.now().toDate();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  public String getCreatedUser() {
    return createdUser;
  }

  public void setCreatedUser(String createdUser) {
    this.createdUser = createdUser;
  }
}
