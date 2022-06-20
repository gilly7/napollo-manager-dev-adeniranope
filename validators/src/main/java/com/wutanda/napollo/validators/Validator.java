package com.wutanda.napollo.validators;

import com.wutanda.napollo.common.exception.NapolloException;

public interface Validator<T> {

  Boolean validate(T data) throws NapolloException;
}
