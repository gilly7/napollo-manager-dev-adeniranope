package com.wutanda.napollo.common.managers;

import com.wutanda.napollo.common.authorization.Authorization;
import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.common.transport.AbstractResponse;
import org.springframework.data.domain.Page;

public interface AbstractManager<T, U> {

  AbstractResponse create(final Authorization authorization, T request) throws NapolloException;

  AbstractResponse<U> get(final Authorization authorization, final String identity)
      throws NapolloException;

  AbstractResponse<Page<U>> list(
      final Authorization authorization, final Integer page, final Integer size)
      throws NapolloException;

  AbstractResponse update(final Authorization authorization, final String identity, T request)
      throws NapolloException;

  AbstractResponse delete(final Authorization authorization, final String identity)
      throws NapolloException;
}
