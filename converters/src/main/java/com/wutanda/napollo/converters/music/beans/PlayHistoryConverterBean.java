package com.wutanda.napollo.converters.music.beans;

import com.wutanda.napollo.api.v1_0.music.common.Play;
import com.wutanda.napollo.converters.accountusers.AccountUserConverter;
import com.wutanda.napollo.converters.music.MediaConverter;
import com.wutanda.napollo.converters.music.PlayHistoryConverter;
import com.wutanda.napollo.persistence.music.PlayHistoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PlayHistoryConverterBean implements PlayHistoryConverter {

  @Autowired private MediaConverter mediaConverter;

  @Autowired private AccountUserConverter accountUserConverter;

  @Override
  public Optional<PlayHistoryEntity> createEntity(Void request) {
    return Optional.empty();
  }

  @Override
  public Optional<Play> createResponse(PlayHistoryEntity entity) {
    if (entity != null) {
      final Play play = new Play();
      play.setHistoryDate(entity.getHistoryDate());
      play.setMedia(this.mediaConverter.createResponse(entity.getMedia()).orElse(null));
      play.setId(entity.getId());
      play.setPlays(entity.getPlays());
      play.setAccountUser(
          accountUserConverter.createResponse(entity.getAccountUser()).orElse(null));
      return Optional.of(play);
    }
    return Optional.empty();
  }
}
