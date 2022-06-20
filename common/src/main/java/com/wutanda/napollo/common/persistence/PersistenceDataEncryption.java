package com.wutanda.napollo.common.persistence;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public final class PersistenceDataEncryption implements AttributeConverter<String, String> {

  private static final String ALGORITHM = "AES";
  private static final String SECRET_KEY = "secret-key-12345";

  private final Key key;
  private final Cipher cipher;

  public PersistenceDataEncryption() throws NoSuchPaddingException, NoSuchAlgorithmException {
    key = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
    cipher = Cipher.getInstance(ALGORITHM);
  }

  /**
   * @param data
   * @return
   */
  @Override
  public String convertToDatabaseColumn(String data) {
    try {
      cipher.init(Cipher.ENCRYPT_MODE, key);
      if (data != null) {
        byte[] b = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(b);
      } else {
        return null;
      }
    } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
      throw new IllegalStateException(e);
    }
  }

  /**
   * @param data
   * @return
   */
  @Override
  public String convertToEntityAttribute(String data) {
    try {
      cipher.init(Cipher.DECRYPT_MODE, key);
      if (data != null) {
        return new String(cipher.doFinal(Base64.getDecoder().decode(data)));
      } else {
        return null;
      }
    } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
      throw new IllegalStateException(e);
    }
  }
}
