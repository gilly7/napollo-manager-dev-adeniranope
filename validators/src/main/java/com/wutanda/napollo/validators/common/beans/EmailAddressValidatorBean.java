package com.wutanda.napollo.validators.common.beans;

import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.validators.common.EmailAddressValidator;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class EmailAddressValidatorBean implements EmailAddressValidator {

  @Override
  public Boolean validate(String data) throws NapolloException {
    try {
      final String emailRegex =
          "^[a-zA-Z0-9_+&*-]+(?:\\\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\\\.)+[a-zA-Z]{2,7}$";
      final Pattern pattern = Pattern.compile(emailRegex);
      return pattern.matcher(data).matches();
    } catch (Exception exception) {
      throw NapolloException.badRequest("Invalid email address. Please check and try again");
    }
  }
}
