package com.wutanda.napollo.managers.music.beans;

import com.google.common.io.Files;
import com.wutanda.napollo.api.v1_0.music.AlbumRequest;
import com.wutanda.napollo.api.v1_0.music.common.Album;
import com.wutanda.napollo.common.activity.ActivityRequest;
import com.wutanda.napollo.common.activity.ActivityType;
import com.wutanda.napollo.common.authorization.Authorization;
import com.wutanda.napollo.common.authorization.PermissionRegistry;
import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.common.loggers.LogLevel;
import com.wutanda.napollo.common.transport.AbstractResponse;
import com.wutanda.napollo.converters.music.AlbumConverter;
import com.wutanda.napollo.managers.activity.ActivityManager;
import com.wutanda.napollo.managers.authorization.PermissionManager;
import com.wutanda.napollo.managers.loggers.LoggerManager;
import com.wutanda.napollo.managers.music.AlbumManager;
import com.wutanda.napollo.persistence.accountusers.dao.AccountUserEntityDao;
import com.wutanda.napollo.persistence.music.AlbumEntity;
import com.wutanda.napollo.persistence.music.MediaEntity;
import com.wutanda.napollo.persistence.music.dao.AlbumEntityDao;
import com.wutanda.napollo.persistence.music.dao.MediaEntityDao;
import com.wutanda.napollo.persistence.users.UserEntity;
import com.wutanda.napollo.validators.common.ImageTypeValidator;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AlbumManagerBean implements AlbumManager {

  @Autowired private AlbumEntityDao albumEntityDao;

  @Autowired private PermissionManager permissionManager;

  @Autowired private LoggerManager loggerManager;

  @Autowired private AccountUserEntityDao accountUserEntityDao;

  @Autowired private AlbumConverter albumConverter;

  @Autowired private ImageTypeValidator imageTypeValidator;

  @Autowired private MediaEntityDao mediaEntityDao;

  @Autowired private ActivityManager activityManager;

  @Value("${napollo.media.image.path}")
  private String mediaImagePath;

  @Override
  public AbstractResponse create(Authorization authorization, AlbumRequest request)
      throws NapolloException {
    return null;
  }

  @Override
  @Transactional
  public AbstractResponse create(
      Authorization authorization, MultipartFile multipartFile, AlbumRequest albumRequest)
      throws NapolloException {
    this.loggerManager.log(AlbumManagerBean.class, LogLevel.TRACE, "Creating a new album");
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.CREATE_MEDIA_ALBUM.name()));
    final UserEntity authorizedUserEntity = this.permissionManager.getAuthorizedUser(authorization);
    if (!authorizedUserEntity.getProfile().getProfileType().equals("ARTIST")) {
      throw NapolloException.internalError("Only artists are allowed to create an album");
    }
    if (this.albumEntityDao
        .findByNameAndOwnerAccountUser_User_Id(albumRequest.getName(), authorizedUserEntity.getId())
        .isPresent()) {
      throw NapolloException.internalError("Album with name already created by this user");
    }
    final AlbumEntity albumEntity = new AlbumEntity();
    albumEntity.setDescription(albumRequest.getDescription());
    albumEntity.setName(albumRequest.getName());
    albumEntity.setYear(albumRequest.getYear());
    albumEntity.setOwnerAccountUser(
        this.accountUserEntityDao
            .findByUser_EmailAddress(authorizedUserEntity.getEmailAddress())
            .orElseThrow(
                () ->
                    NapolloException.internalError(
                        "Unable to complete this operation. Please check with the administrator")));
    if (multipartFile != null && !multipartFile.isEmpty()) {
      this.imageTypeValidator.validate(multipartFile);
      try {
        final String albumArtName = RandomStringUtils.randomAlphanumeric(64);
        final String fileExtensionName =
            Files.getFileExtension(multipartFile.getOriginalFilename());
        multipartFile.transferTo(
            Paths.get(
                mediaImagePath
                    .concat("/")
                    .concat(albumArtName)
                    .concat(".")
                    .concat(fileExtensionName)));
        albumEntity.setUrl(albumArtName.concat(".").concat(fileExtensionName));
      } catch (IOException ioException) {
        this.loggerManager.log(
            AlbumManagerBean.class, LogLevel.ERROR, ioException.getMessage(), ioException);
        throw NapolloException.internalError("Unable to process the album art information");
      }
    }
    this.albumEntityDao.save(albumEntity);
    final ActivityRequest activityRequest = new ActivityRequest();
    activityRequest.setActivityType(ActivityType.CREATE_ALBUM);
    activityRequest.setActivitySourceOwnerId(albumEntity.getId());
    activityRequest.setActivityUserId(authorizedUserEntity.getId());
    activityManager.postActivity(authorization, activityRequest);
    return AbstractResponse.response("Album created successfully", Boolean.TRUE);
  }

  @Override
  @Transactional
  public AbstractResponse update(
      Authorization authorization,
      String albumIdentity,
      MultipartFile multipartFile,
      AlbumRequest albumRequest)
      throws NapolloException {
    this.loggerManager.log(AlbumManagerBean.class, LogLevel.TRACE, "Updating a new album");
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.UPDATE_MEDIA_ALBUM.name()));
    final UserEntity authorizedUserEntity = this.permissionManager.getAuthorizedUser(authorization);
    if (!authorizedUserEntity.getProfile().getProfileType().equals("ARTIST")) {
      throw NapolloException.internalError("Only artists are allowed to update an album");
    }
    final AlbumEntity albumEntity =
        this.albumEntityDao
            .findById(albumIdentity)
            .orElseThrow(
                () -> NapolloException.internalError("Invalid album id. Please try again"));
    if (!albumEntity.getOwnerAccountUser().getUser().getId().equals(authorizedUserEntity.getId())) {
      throw NapolloException.internalError(
          "Invalid access. Album not available for this operation");
    }

    if (albumRequest.getName() != null) {
      albumEntity.setName(albumRequest.getName());
    }
    if (albumRequest.getDescription() != null) {
      albumEntity.setDescription(albumRequest.getDescription());
    }
    if (albumRequest.getYear() != null) {
      albumEntity.setYear(albumRequest.getYear());
    }
    if (multipartFile != null && !multipartFile.isEmpty()) {
      this.imageTypeValidator.validate(multipartFile);
      try {
        final String albumArtName = RandomStringUtils.randomAlphanumeric(64);
        final String fileExtensionName =
            Files.getFileExtension(multipartFile.getOriginalFilename());
        multipartFile.transferTo(
            Paths.get(
                mediaImagePath
                    .concat("/")
                    .concat(albumArtName)
                    .concat(".")
                    .concat(fileExtensionName)));
        final Path mediaPath = Paths.get(mediaImagePath.concat("/").concat(albumEntity.getUrl()));
        mediaPath.toFile().delete();
        albumEntity.setUrl(albumArtName.concat(".").concat(fileExtensionName));
      } catch (IOException ioException) {
        throw NapolloException.internalError("Unable to process the album art information");
      }
    }
    return AbstractResponse.response("Album updated successfully", Boolean.TRUE);
  }

  @Override
  @Transactional
  public AbstractResponse addMedia(
      Authorization authorization, String albumIdentity, String mediaIdentity)
      throws NapolloException {
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.CREATE_MEDIA.name()));
    final MediaEntity mediaEntity =
        mediaEntityDao
            .findById(mediaIdentity)
            .orElseThrow(() -> NapolloException.internalError("Media is not available"));
    final AlbumEntity albumEntity =
        albumEntityDao
            .findById(albumIdentity)
            .orElseThrow(() -> NapolloException.internalError("Album is not available"));
    if (!albumEntity.getMedia().contains(mediaEntity)) {
      albumEntity.getMedia().add(mediaEntity);
    }
    albumEntityDao.save(albumEntity);
    final ActivityRequest activityRequest = new ActivityRequest();
    activityRequest.setActivityType(ActivityType.ADD_ALBUM_MEDIA);
    activityRequest.setActivitySourceOwnerId(mediaEntity.getId());
    activityRequest.setActivityDestinationOwnerId(albumEntity.getId());
    activityRequest.setActivityUserId(permissionManager.getAuthorizedUser(authorization).getId());
    activityManager.postActivity(authorization, activityRequest);
    return AbstractResponse.response("Album updated successfully", Boolean.TRUE);
  }

  @Override
  public AbstractResponse<Album> get(Authorization authorization, String identity)
      throws NapolloException {
    this.loggerManager.log(AlbumManagerBean.class, LogLevel.TRACE, "Getting an album information");
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.GET_MEDIA_ALBUM.name()));
    final AlbumEntity albumEntity =
        this.albumEntityDao
            .findById(identity)
            .orElseThrow(() -> NapolloException.internalError("Album information not available"));
    return AbstractResponse.response(
        this.albumConverter.createResponse(albumEntity),
        "Album information fetched successfully",
        Boolean.TRUE);
  }

  @Override
  public AbstractResponse<Page<Album>> list(Authorization authorization, Integer page, Integer size)
      throws NapolloException {
    this.loggerManager.log(AlbumManagerBean.class, LogLevel.TRACE, "Listing all album information");
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.GET_MEDIA_ALBUMS.name()));
    final UserEntity authorizedUserEntity = this.permissionManager.getAuthorizedUser(authorization);
    final Page<AlbumEntity> albumEntityPage =
        this.albumEntityDao
            .findByOwnerAccountUser_User_Id(
                authorizedUserEntity.getId(), PageRequest.of(page, size))
            .orElseThrow(() -> NapolloException.internalError("Unable to fetch album information"));
    final Page<Album> albumPage =
        new PageImpl(
            albumEntityPage.stream()
                .map(this.albumConverter::createResponse)
                .map(Optional::get)
                .collect(Collectors.toList()),
            albumEntityPage.getPageable(),
            albumEntityPage.getTotalElements());
    return AbstractResponse.response(
        albumPage, "Album information fetched successfully", Boolean.TRUE);
  }

  @Override
  public AbstractResponse update(Authorization authorization, String identity, AlbumRequest request)
      throws NapolloException {
    return null;
  }

  @Override
  @Transactional
  public AbstractResponse delete(Authorization authorization, String identity)
      throws NapolloException {
    this.loggerManager.log(AlbumManagerBean.class, LogLevel.TRACE, "Removing an album information");
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.REMOVE_MEDIA_ALBUM.name()));
    final UserEntity authorizedUserEntity = this.permissionManager.getAuthorizedUser(authorization);
    final AlbumEntity albumEntity =
        this.albumEntityDao
            .findById(identity)
            .orElseThrow(() -> NapolloException.internalError("Album information not available"));
    if (!albumEntity.getOwnerAccountUser().getUser().getId().equals(authorizedUserEntity.getId())) {
      throw NapolloException.internalError(
          "Invalid access. Album not available for this operation");
    }
    final Path mediaPath = Paths.get(mediaImagePath.concat("/").concat(albumEntity.getUrl()));
    mediaPath.toFile().delete();
    albumEntityDao.delete(albumEntity);
    return AbstractResponse.response("Album removed successfully", Boolean.TRUE);
  }
}
