package com.wutanda.napollo.converters.music.beans;

import com.wutanda.napollo.api.v1_0.music.PlaylistRequest;
import com.wutanda.napollo.api.v1_0.music.common.Playlist;
import com.wutanda.napollo.converters.music.MediaConverter;
import com.wutanda.napollo.converters.music.PlaylistConverter;
import com.wutanda.napollo.persistence.music.PlaylistEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PlaylistConverterBean implements PlaylistConverter {

  @Autowired private MediaConverter mediaConverter;

  @Value("${napollo.media.image.url}")
  private String mediaImageUrl;

  @Override
  public Optional<PlaylistEntity> createEntity(PlaylistRequest request) {
    return Optional.empty();
  }

  @Override
  public Optional<Playlist> createResponse(PlaylistEntity entity) {
    if (entity != null) {
      final Playlist playlist = new Playlist();
      playlist.setDescription(entity.getDescription());
      playlist.setName(entity.getName());
      if (entity.getUrl() != null) {
        playlist.setUrl(mediaImageUrl.concat(entity.getUrl()));
      }
      playlist.setVisible(entity.getVisible());
      playlist.setMedia(
          entity.getMedia().stream()
              .map(this.mediaConverter::createResponse)
              .map(Optional::get)
              .collect(Collectors.toList()));
      playlist.setId(entity.getId());
      playlist.setTimestamp(entity.getTimestamp());
      return Optional.of(playlist);
    }
    return Optional.empty();
  }
}
