package com.wutanda.napollo.converters.music.beans;

import com.wutanda.napollo.api.v1_0.music.GenreRequest;
import com.wutanda.napollo.api.v1_0.music.common.Genre;
import com.wutanda.napollo.converters.music.GenreConverter;
import com.wutanda.napollo.persistence.music.GenreEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GenreConverterBean implements GenreConverter {

  @Override
  public Optional<GenreEntity> createEntity(GenreRequest request) {
    if (request != null) {
      final GenreEntity genreEntity = new GenreEntity();
      genreEntity.setStatus(Boolean.TRUE);
      genreEntity.setDescription(request.getDescription());
      genreEntity.setName(request.getName());
      return Optional.ofNullable(genreEntity);
    }
    return Optional.empty();
  }

  @Override
  public Optional<Genre> createResponse(GenreEntity entity) {
    if (entity != null) {
      final Genre genre = new Genre();
      genre.setId(entity.getId());
      genre.setDescription(entity.getDescription());
      genre.setTimestamp(entity.getTimestamp());
      genre.setName(entity.getName());
      return Optional.ofNullable(genre);
    }
    return Optional.empty();
  }
}
