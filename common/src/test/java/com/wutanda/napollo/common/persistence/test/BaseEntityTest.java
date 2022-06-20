package com.wutanda.napollo.common.persistence.test;

import com.wutanda.napollo.common.persistence.BaseEntity;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BaseEntityTest {

  @Test
  public void testValidBaseEntity() {
    final BaseEntityTest.DataEntity dataEntity = new DataEntity();
    dataEntity.setDataName("name");
    dataEntity.setDataValue("value");
    assertThat(dataEntity.getId()).isNotNull();
    assertThat(dataEntity.getDataName()).isEqualTo("name");
    assertThat(dataEntity.getDataValue()).isEqualTo("value");
  }

  public static final class DataEntity extends BaseEntity {

    private String dataName;
    private String dataValue;

    public String getDataName() {
      return dataName;
    }

    public void setDataName(String dataName) {
      this.dataName = dataName;
    }

    public String getDataValue() {
      return dataValue;
    }

    public void setDataValue(String dataValue) {
      this.dataValue = dataValue;
    }
  }
}
