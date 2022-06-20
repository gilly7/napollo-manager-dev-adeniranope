package com.wutanda.napollo.converters.activity;

import com.wutanda.napollo.api.v1_0.activity.common.Activity;
import com.wutanda.napollo.converters.accountusers.AccountUserConverter;
import com.wutanda.napollo.converters.music.AlbumConverter;
import com.wutanda.napollo.converters.music.CommentConverter;
import com.wutanda.napollo.converters.music.MediaConverter;
import com.wutanda.napollo.converters.music.PlaylistConverter;
import com.wutanda.napollo.persistence.accountusers.dao.AccountUserEntityDao;
import com.wutanda.napollo.persistence.activity.ActivityEntity;
import com.wutanda.napollo.persistence.music.dao.AlbumEntityDao;
import com.wutanda.napollo.persistence.music.dao.CommentEntityDao;
import com.wutanda.napollo.persistence.music.dao.MediaEntityDao;
import com.wutanda.napollo.persistence.music.dao.PlaylistEntityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ActivityConverterBean implements ActivityConverter {

  @Autowired private MediaEntityDao mediaEntityDao;

  @Autowired private MediaConverter mediaConverter;

  @Autowired private AlbumConverter albumConverter;

  @Autowired private AccountUserConverter accountUserConverter;

  @Autowired private AccountUserEntityDao accountUserEntityDao;

  @Autowired private AlbumEntityDao albumEntityDao;

  @Autowired private PlaylistEntityDao playlistEntityDao;

  @Autowired private PlaylistConverter playlistConverter;

  @Autowired private CommentConverter commentConverter;

  @Autowired private CommentEntityDao commentEntityDao;

  @Override
  public Optional<ActivityEntity> createEntity(Void request) {
    return Optional.empty();
  }

  @Override
  public Optional<Activity> createResponse(ActivityEntity entity) {
    if (entity != null) {
      final Activity activity = new Activity();
      activity.setActivityType(entity.getActivityType());
      activity.setActivityDateTime(entity.getTimestamp());
      activity.setCreatingAccountUser(
          accountUserConverter
              .createResponse(
                  accountUserEntityDao.findByUser_Id(entity.getCreatedUser()).orElse(null))
              .orElse(null));
      switch (entity.getActivityType()) {
        case "CREATE_ALBUM":
          activity.setAlbum(
              albumConverter
                  .createResponse(albumEntityDao.findById(entity.getActivityOwnerId()).orElse(null))
                  .orElse(null));
          break;
        case "CREATE_MEDIA":
          activity.setMedia(
              mediaConverter
                  .createResponse(mediaEntityDao.findById(entity.getActivityOwnerId()).orElse(null))
                  .orElse(null));
          break;
        case "CREATE_PLAYLIST":
          activity.setPlaylist(
              playlistConverter
                  .createResponse(
                      playlistEntityDao.findById(entity.getActivityOwnerId()).orElse(null))
                  .orElse(null));
        case "ADD_ALBUM_MEDIA":
          activity.setMedia(
              mediaConverter
                  .createResponse(
                      mediaEntityDao.findById(entity.getActivityDestinationOwnerId()).orElse(null))
                  .orElse(null));
          activity.setAlbum(
              albumConverter
                  .createResponse(albumEntityDao.findById(entity.getActivityOwnerId()).orElse(null))
                  .orElse(null));
          break;
        case "ADD_PLAYLIST_MEDIA":
          activity.setMedia(
              mediaConverter
                  .createResponse(
                      mediaEntityDao.findById(entity.getActivityDestinationOwnerId()).orElse(null))
                  .orElse(null));
          activity.setPlaylist(
              playlistConverter
                  .createResponse(playlistEntityDao.getOne(entity.getActivityOwnerId()))
                  .orElse(null));
          break;
        case "COMMENT":
          activity.setComment(
              commentConverter
                  .createResponse(commentEntityDao.getOne(entity.getActivityOwnerId()))
                  .orElse(null));
          break;
        case "REPLY":
          activity.setReply(
              commentConverter
                  .createResponse(commentEntityDao.getOne(entity.getActivityOwnerId()))
                  .orElse(null));
          break;
        case "FOLLOW":
          activity.setAccountUser(
              accountUserConverter
                  .createResponse(accountUserEntityDao.getOne(entity.getActivityOwnerId()))
                  .orElse(null));
          break;
      }
      return Optional.ofNullable(activity);
    }
    return Optional.empty();
  }
}
