package com.wutanda.napollo.validators.common.beans;

import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.validators.common.URLValidator;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class URLValidatorBean implements URLValidator {

  @Override
  public Boolean validate(String data) throws NapolloException {
    try {
      final String urlRegex =
          "^((((https?|ftps?|gopher|telnet|nntp)://)|(mailto:|news:))(%[0-9A-Fa-f]{2}|[-()_.!~*';/?:@&=+$,A-Za-z0-9])+)([).!';/?:,][[:blank:]])?$";
      return Pattern.compile(urlRegex).matcher(data).matches();
    } catch (Exception exception) {
      throw NapolloException.badRequest("URL is not valid");
    }
  }
}
