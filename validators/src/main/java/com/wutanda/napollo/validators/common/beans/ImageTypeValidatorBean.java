package com.wutanda.napollo.validators.common.beans;

import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.validators.common.ImageTypeValidator;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

@Component
public class ImageTypeValidatorBean implements ImageTypeValidator {

  @Override
  public Boolean validate(MultipartFile data) throws NapolloException {
    if (!Arrays.asList("image/png", "image/jpeg", "image/svg+xml")
        .contains(data.getContentType())) {
      throw NapolloException.badRequest(
          "Invalid image file format. Only PNG, JPEG and SVG formats are allowed");
    }
    return true;
  }
}
