package com.wutanda.napollo.managers.music.beans;

import com.google.common.io.Files;
import com.wutanda.napollo.api.v1_0.music.PlaylistRequest;
import com.wutanda.napollo.api.v1_0.music.common.Playlist;
import com.wutanda.napollo.common.activity.ActivityRequest;
import com.wutanda.napollo.common.activity.ActivityType;
import com.wutanda.napollo.common.authorization.Authorization;
import com.wutanda.napollo.common.authorization.PermissionRegistry;
import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.common.loggers.LogLevel;
import com.wutanda.napollo.common.transport.AbstractResponse;
import com.wutanda.napollo.converters.music.PlaylistConverter;
import com.wutanda.napollo.managers.activity.ActivityManager;
import com.wutanda.napollo.managers.authorization.PermissionManager;
import com.wutanda.napollo.managers.loggers.LoggerManager;
import com.wutanda.napollo.managers.music.PlaylistManager;
import com.wutanda.napollo.persistence.accountusers.AccountUserEntity;
import com.wutanda.napollo.persistence.accountusers.dao.AccountUserEntityDao;
import com.wutanda.napollo.persistence.music.MediaEntity;
import com.wutanda.napollo.persistence.music.PlaylistEntity;
import com.wutanda.napollo.persistence.music.dao.MediaEntityDao;
import com.wutanda.napollo.persistence.music.dao.PlaylistEntityDao;
import com.wutanda.napollo.validators.common.ImageTypeValidator;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PlaylistManagerBean implements PlaylistManager {

  @Autowired private PlaylistEntityDao playlistEntityDao;

  @Autowired private PermissionManager permissionManager;

  @Autowired private MediaEntityDao mediaEntityDao;

  @Autowired private AccountUserEntityDao accountUserEntityDao;

  @Autowired private ImageTypeValidator imageTypeValidator;

  @Autowired private LoggerManager loggerManager;

  @Autowired private PlaylistConverter playlistConverter;

  @Autowired private ActivityManager activityManager;

  @Value("${napollo.media.image.path}")
  private String mediaImagePath;

  @Override
  @Transactional
  public AbstractResponse create(Authorization authorization, PlaylistRequest request)
      throws NapolloException {
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.CREATE_MEDIA_PLAYLIST.name()));
    if (this.playlistEntityDao
        .findByNameAndAccountUser_User_EmailAddress(
            request.getName(),
            this.permissionManager.getAuthorizedUser(authorization).getEmailAddress())
        .isPresent()) {
      throw NapolloException.internalError("A playlist with name already exists.");
    }
    final PlaylistEntity playlistEntity = new PlaylistEntity();
    playlistEntity.setDescription(request.getDescription());
    playlistEntity.setAccountUser(
        this.accountUserEntityDao
            .findByUser_EmailAddress(
                this.permissionManager.getAuthorizedUser(authorization).getEmailAddress())
            .orElseThrow(() -> NapolloException.internalError("Unable to create playlist")));
    playlistEntity.setName(request.getName());
    playlistEntity.setVisible(request.getVisible());
    // upload
    if (request.getArtMultipartFile() != null && !request.getArtMultipartFile().isEmpty()) {
      imageTypeValidator.validate(request.getArtMultipartFile());
      try {
        final String mediaName = RandomStringUtils.randomAlphanumeric(64);
        final String fileExtensionName =
            Files.getFileExtension(request.getArtMultipartFile().getOriginalFilename());
        request
            .getArtMultipartFile()
            .transferTo(
                Paths.get(
                    mediaImagePath
                        .concat("/")
                        .concat(mediaName)
                        .concat(".")
                        .concat(fileExtensionName)));
        playlistEntity.setUrl(mediaName.concat(".").concat(fileExtensionName));
      } catch (Exception ioException) {
        this.loggerManager.log(
            MediaManagerBean.class, LogLevel.ERROR, ioException.getMessage(), ioException);
        throw NapolloException.internalError("Unable to process the media file");
      }
    }
    playlistEntity.setMedia(new ArrayList<>());
    playlistEntity.setCreatedUser(this.permissionManager.getAuthorizedUser(authorization).getId());
    this.playlistEntityDao.save(playlistEntity);

    final ActivityRequest activityRequest = new ActivityRequest();
    activityRequest.setActivityType(ActivityType.CREATE_PLAYLIST);
    activityRequest.setActivitySourceOwnerId(playlistEntity.getId());
    activityRequest.setActivityDestinationOwnerId(playlistEntity.getId());
    activityRequest.setActivityUserId(permissionManager.getAuthorizedUser(authorization).getId());
    activityManager.postActivity(authorization, activityRequest);

    return AbstractResponse.response("Playlist created successfully", Boolean.TRUE);
  }

  @Override
  public AbstractResponse<Playlist> get(Authorization authorization, String identity)
      throws NapolloException {
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.GET_MEDIA_PLAYLIST.name()));
    final PlaylistEntity playlistEntity =
        this.playlistEntityDao
            .findById(identity)
            .orElseThrow(() -> NapolloException.internalError("Playlist is not available"));
    final AccountUserEntity accountUserEntity =
        this.accountUserEntityDao
            .findByUser_EmailAddress(
                this.permissionManager.getAuthorizedUser(authorization).getEmailAddress())
            .orElseThrow(() -> NapolloException.internalError("Unable to fetch the account user"));
    if (!playlistEntity.getAccountUser().getId().equals(accountUserEntity.getId())) {
      throw NapolloException.internalError("Playlist is not accessible to this user");
    }
    return AbstractResponse.response(
        this.playlistConverter
            .createResponse(playlistEntity)
            .orElseThrow(() -> NapolloException.internalError("Unable to complete this operation")),
        "Playlist fetched successfully",
        Boolean.TRUE);
  }

  @Override
  public AbstractResponse<Page<Playlist>> list(
      Authorization authorization, Integer page, Integer size) throws NapolloException {
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.GET_MEDIA_PLAYLISTS.name()));
    Page<PlaylistEntity> playlistEntityPage = null;
    if (permissionManager.isAccountUser(authorization)) {
      playlistEntityPage =
          this.playlistEntityDao.findByAccountUser_User_EmailAddress(
              this.permissionManager.getAuthorizedUser(authorization).getEmailAddress(),
              PageRequest.of(page, size));
    } else if (permissionManager.isAdministrator(authorization)) {
      playlistEntityPage = this.playlistEntityDao.findAll(PageRequest.of(page, size));
    }
    final Page<Playlist> playlistPage =
        new PageImpl(
            playlistEntityPage.stream()
                .map(this.playlistConverter::createResponse)
                .map(Optional::get)
                .collect(Collectors.toList()),
            playlistEntityPage.getPageable(),
            playlistEntityPage.getTotalElements());
    return AbstractResponse.response(playlistPage, "Playlist listed successfully", Boolean.TRUE);
  }

  @Override
  @Transactional
  public AbstractResponse update(
      Authorization authorization, String identity, PlaylistRequest request)
      throws NapolloException {
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.UPDATE_MEDIA_PLAYLIST.name()));
    final PlaylistEntity playlistEntity =
        this.playlistEntityDao
            .findById(identity)
            .orElseThrow(() -> NapolloException.internalError("Playlist is not available"));
    if (request.getVisible() != null) {
      playlistEntity.setVisible(request.getVisible());
    }
    if (request.getDescription() != null) {
      playlistEntity.setDescription(request.getDescription());
    }
    if (request.getName() != null) {
      playlistEntity.setName(request.getName());
    }
    if (request.getArtMultipartFile() != null && !request.getArtMultipartFile().isEmpty()) {
      imageTypeValidator.validate(request.getArtMultipartFile());
      final File artPath =
          Paths.get(mediaImagePath.concat("/").concat(playlistEntity.getUrl())).toFile();
      try {
        final String mediaName = RandomStringUtils.randomAlphanumeric(64);
        final String fileExtensionName =
            Files.getFileExtension(request.getArtMultipartFile().getOriginalFilename());
        request
            .getArtMultipartFile()
            .transferTo(
                Paths.get(
                    mediaImagePath
                        .concat("/")
                        .concat(mediaName)
                        .concat(".")
                        .concat(fileExtensionName)));
        playlistEntity.setUrl(mediaName.concat(".").concat(fileExtensionName));
        artPath.delete();
      } catch (IOException ioException) {
        this.loggerManager.log(
            MediaManagerBean.class, LogLevel.ERROR, ioException.getMessage(), ioException);
        throw NapolloException.internalError("Unable to process the media file");
      }
    }
    this.playlistEntityDao.save(playlistEntity);
    return AbstractResponse.response("Playlist saved successfully", Boolean.TRUE);
  }

  @Override
  @Transactional
  public AbstractResponse delete(Authorization authorization, String identity)
      throws NapolloException {
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.DELETE_MEDIA_PLAYLIST.name()));
    final PlaylistEntity playlistEntity =
        this.playlistEntityDao
            .findById(identity)
            .orElseThrow(() -> NapolloException.internalError("Playlist is not available"));
    this.playlistEntityDao.delete(playlistEntity);
    return AbstractResponse.response("Playlist deleted successfully", Boolean.TRUE);
  }

  @Override
  @Transactional
  public AbstractResponse addMedia(
      Authorization authorization, String playlistIdentity, String mediaIdentity, Boolean state)
      throws NapolloException {
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.CREATE_MEDIA_PLAYLIST.name()));
    final MediaEntity mediaEntity =
        this.mediaEntityDao
            .findById(mediaIdentity)
            .orElseThrow(() -> NapolloException.internalError("Media is not available"));
    final PlaylistEntity playlistEntity =
        this.playlistEntityDao
            .findById(playlistIdentity)
            .orElseThrow(() -> NapolloException.internalError("Playlist is not available"));
    if (state) {
      playlistEntity.getMedia().add(mediaEntity);
    } else {
      playlistEntity.getMedia().remove(mediaEntity);
    }
    this.playlistEntityDao.save(playlistEntity);

    final ActivityRequest activityRequest = new ActivityRequest();
    activityRequest.setActivityType(ActivityType.ADD_PLAYLIST_MEDIA);
    activityRequest.setActivitySourceOwnerId(playlistEntity.getId());
    activityRequest.setActivityDestinationOwnerId(mediaEntity.getId());
    activityRequest.setActivityUserId(permissionManager.getAuthorizedUser(authorization).getId());
    activityManager.postActivity(authorization, activityRequest);

    return AbstractResponse.response("Playlist updated successfully", Boolean.TRUE);
  }

  @Override
  public AbstractResponse<Page<Playlist>> listByStatus(
      Authorization authorization, Boolean state, Integer page, Integer size)
      throws NapolloException {
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.GET_MEDIA_PLAYLISTS.name()));
    Page<PlaylistEntity> playlistEntityPage = null;
    if (permissionManager.isAdministrator(authorization)) {
      playlistEntityPage = this.playlistEntityDao.findByVisible(state, PageRequest.of(page, size));
    } else if (permissionManager.isAccountUser(authorization)) {
      if (state != null && state) { // public playlists
        playlistEntityPage = this.playlistEntityDao.findByVisibleTrue(PageRequest.of(page, size));
      } else { // list all account user playlists
        playlistEntityPage =
            this.playlistEntityDao.findByAccountUser_User_EmailAddress(
                this.permissionManager.getAuthorizedUser(authorization).getEmailAddress(),
                PageRequest.of(page, size));
      }
    }
    final Page<Playlist> playlistPage =
        new PageImpl(
            playlistEntityPage.stream()
                .map(this.playlistConverter::createResponse)
                .map(Optional::get)
                .collect(Collectors.toList()),
            playlistEntityPage.getPageable(),
            playlistEntityPage.getTotalElements());
    return AbstractResponse.response(playlistPage, "Playlist listed successfully", Boolean.TRUE);
  }
}
