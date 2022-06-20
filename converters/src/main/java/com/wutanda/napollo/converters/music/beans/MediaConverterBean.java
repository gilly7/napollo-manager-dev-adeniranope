package com.wutanda.napollo.converters.music.beans;

import com.wutanda.napollo.api.v1_0.music.MediaRequest;
import com.wutanda.napollo.api.v1_0.music.common.Media;
import com.wutanda.napollo.converters.accountusers.AccountUserConverter;
import com.wutanda.napollo.converters.music.AlbumConverter;
import com.wutanda.napollo.converters.music.GenreConverter;
import com.wutanda.napollo.converters.music.MediaConverter;
import com.wutanda.napollo.persistence.music.MediaEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class MediaConverterBean implements MediaConverter {

  @Autowired private GenreConverter genreConverter;

  @Autowired private AlbumConverter albumConverter;

  @Autowired private AccountUserConverter accountUserConverter;

  @Value("${napollo.media.image.url}")
  private String mediaImagePath;

  @Value("${napollo.media.file.url}")
  private String mediaFilePath;

  @Override
  public Optional<MediaEntity> createEntity(MediaRequest request) {
    return Optional.empty();
  }

  @Override
  public Optional<Media> createResponse(MediaEntity entity) {
    if (entity != null) {
      final Media media = new Media();
      media.setId(entity.getId());
      media.setTimestamp(entity.getTimestamp());
      media.setGenre(this.genreConverter.createResponse(entity.getGenre()).orElse(null));
      media.setAlbum(this.albumConverter.createResponse(entity.getAlbum()).orElse(null));
      if (entity.getTaggedAccountUsers() != null) {
        media.setTaggedAccountUsers(
            entity.getTaggedAccountUsers().stream()
                .map(this.accountUserConverter::createResponse)
                .map(Optional::get)
                .collect(Collectors.toList()));
      }
      media.setTrailerDuration(entity.getTrailerDuration());
      media.setTrailer(mediaFilePath.concat(entity.getTrailerUrl()));
      media.setDescription(entity.getDescription());
      media.setComments(entity.getComments());
      media.setUrl(mediaFilePath.concat(entity.getMediaUrl()));
      media.setHits(entity.getHits());
      media.setTitle(entity.getName());
      if (entity.getPhotoUrl() != null) {
    	  media.setImage(mediaImagePath.concat(entity.getPhotoUrl()));
      }
      media.setFeaturedArtists(entity.getFeaturedArtists());
      media.setLikes(entity.getLikes());
      media.setOwnerAccountUser(
          this.accountUserConverter.createResponse(entity.getAccountUser()).orElse(null));
      media.setDiscovered(entity.getDiscovered());
      media.setShortId(entity.getShortId());
      return Optional.of(media);
    }
    return Optional.empty();
  }
}
