package com.wutanda.napollo.converters;

import java.util.Optional;

public interface Converter<T, U, X> {

  Optional<X> createEntity(T request);

  Optional<U> createResponse(X entity);
}
