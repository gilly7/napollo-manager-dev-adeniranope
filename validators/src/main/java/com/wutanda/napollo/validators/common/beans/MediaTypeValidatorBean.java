package com.wutanda.napollo.validators.common.beans;

import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.validators.common.MediaTypeValidator;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

@Component
public class MediaTypeValidatorBean implements MediaTypeValidator {

  @Override
  public Boolean validate(MultipartFile data) throws NapolloException {
    if (!Arrays.asList("audio/mpeg", "video/mp4").contains(data.getContentType())) {
      throw NapolloException.badRequest(
          "Invalid music file format. Only MP3 and MP4 formats are allowed");
    }
    return true;
  }
}
