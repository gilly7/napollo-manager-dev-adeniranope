package com.wutanda.napollo.common.persistence.test;

import com.wutanda.napollo.common.persistence.PersistenceDataEncryption;
import org.junit.Test;
import org.opentest4j.AssertionFailedError;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.assertThat;

public class PersistenceDataEncryptionTest {

  @Test
  public void testValidEncryptionAndDecryption()
      throws NoSuchPaddingException, NoSuchAlgorithmException {
    final PersistenceDataEncryption persistenceDataEncryption = new PersistenceDataEncryption();
    final String encryptedData = persistenceDataEncryption.convertToDatabaseColumn("FirstName");
    assertThat(encryptedData).isNotNull();
    final String decryptedData = persistenceDataEncryption.convertToEntityAttribute(encryptedData);
    assertThat(decryptedData).isEqualTo("FirstName");
  }

  @Test(expected = AssertionFailedError.class)
  public void testInValidEncryptionAndDecryption()
      throws NoSuchPaddingException, NoSuchAlgorithmException {
    final PersistenceDataEncryption persistenceDataEncryption = new PersistenceDataEncryption();
    final String encryptedData = persistenceDataEncryption.convertToDatabaseColumn("FirstName");
    assertThat(encryptedData).isNotNull();
    final String decryptedData = persistenceDataEncryption.convertToEntityAttribute(encryptedData);
    assertThat(decryptedData).isEqualTo("LastName");
  }
}
