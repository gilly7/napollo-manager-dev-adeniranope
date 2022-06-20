package com.wutanda.napollo.converters.music.beans;

import com.wutanda.napollo.api.v1_0.music.AlbumRequest;
import com.wutanda.napollo.api.v1_0.music.common.Album;
import com.wutanda.napollo.converters.accountusers.AccountUserConverter;
import com.wutanda.napollo.converters.music.AlbumConverter;
import com.wutanda.napollo.persistence.music.AlbumEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AlbumConverterBean implements AlbumConverter {

  @Value("${napollo.media.image.url}")
  private String mediaImageUrl;

  @Autowired private AccountUserConverter accountUserConverter;

  @Override
  public Optional<AlbumEntity> createEntity(AlbumRequest request) {
    return Optional.empty();
  }

  @Override
  public Optional<Album> createResponse(AlbumEntity entity) {
    if (entity != null) {
      final Album album = new Album();
      album.setDescription(entity.getDescription());
      album.setName(entity.getName());
      album.setId(entity.getId());
      album.setTimestamp(entity.getTimestamp());
      album.setYear(entity.getYear());
      album.setOwner(
          this.accountUserConverter.createResponse(entity.getOwnerAccountUser()).orElse(null));
      album.setUrl(mediaImageUrl.concat(entity.getUrl()));
      return Optional.ofNullable(album);
    }
    return Optional.empty();
  }
}
