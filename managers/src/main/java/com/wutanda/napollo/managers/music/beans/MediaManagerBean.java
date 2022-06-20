package com.wutanda.napollo.managers.music.beans;

import com.google.common.io.Files;
import com.wutanda.napollo.api.v1_0.accountusers.common.AccountUser;
import com.wutanda.napollo.api.v1_0.music.MediaRequest;
import com.wutanda.napollo.api.v1_0.music.MediaSearchRequest;
import com.wutanda.napollo.api.v1_0.music.PlayRequest;
import com.wutanda.napollo.api.v1_0.music.common.Media;
import com.wutanda.napollo.api.v1_0.music.common.Play;
import com.wutanda.napollo.common.activity.ActivityRequest;
import com.wutanda.napollo.common.activity.ActivityType;
import com.wutanda.napollo.common.authorization.Authorization;
import com.wutanda.napollo.common.authorization.PermissionRegistry;
import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.common.loggers.LogLevel;
import com.wutanda.napollo.common.transport.AbstractResponse;
import com.wutanda.napollo.converters.accountusers.AccountUserConverter;
import com.wutanda.napollo.converters.music.MediaConverter;
import com.wutanda.napollo.converters.music.PlayHistoryConverter;
import com.wutanda.napollo.managers.activity.ActivityManager;
import com.wutanda.napollo.managers.authorization.PermissionManager;
import com.wutanda.napollo.managers.loggers.LoggerManager;
import com.wutanda.napollo.managers.music.MediaManager;
import com.wutanda.napollo.persistence.accountusers.AccountUserEntity;
import com.wutanda.napollo.persistence.accountusers.dao.AccountUserEntityDao;
import com.wutanda.napollo.persistence.music.*;
import com.wutanda.napollo.persistence.music.dao.*;
import com.wutanda.napollo.persistence.users.UserEntity;
import com.wutanda.napollo.validators.common.ImageTypeValidator;
import com.wutanda.napollo.validators.common.MediaTypeValidator;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.mp4parser.IsoFile;

@Component
public class MediaManagerBean implements MediaManager {

  @Autowired private MediaEntityDao mediaEntityDao;

  @Autowired private AccountUserEntityDao accountUserEntityDao;

  @Autowired private PermissionManager permissionManager;

  @Autowired private GenreEntityDao genreEntityDao;

  @Autowired private AlbumEntityDao albumEntityDao;

  @Autowired private LoggerManager loggerManager;

  @Autowired private MediaConverter mediaConverter;

  @Autowired private AccountUserConverter accountUserConverter;

  @Autowired private PlayEntityDao playEntityDao;

  @Autowired private PlayHistoryEntityDao playHistoryEntityDao;

  @Autowired private PlayHistoryConverter playHistoryConverter;

  @Autowired private ImageTypeValidator imageTypeValidator;

  @Autowired private MediaTypeValidator mediaTypeValidator;

  @Autowired private CommentEntityDao commentEntityDao;

  @Autowired private ActivityManager activityManager;

  @PersistenceContext private EntityManager entityManager;

  @Value("${napollo.media.image.path}")
  private String mediaImagePath;

  @Value("${napollo.media.file.path}")
  private String mediaFilePath;

  @Override
  @Transactional
  public AbstractResponse<Media> create(Authorization authorization, MediaRequest request)
      throws NapolloException {
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.CREATE_MEDIA.name()));
    final UserEntity authorizedUserEntity = this.permissionManager.getAuthorizedUser(authorization);
    if (!authorizedUserEntity.getProfile().getProfileType().equals("ARTIST")) {
      throw NapolloException.authorizationError("Only artists are allowed to upload media");
    }
    final MediaEntity mediaEntity = new MediaEntity();
    mediaEntity.setStatus("CREATED");
    final GenreEntity genreEntity =
        this.genreEntityDao
            .findById(request.getGenreId())
            .orElseThrow(() -> NapolloException.badRequest("Genre is not available"));
    mediaEntity.setGenre(genreEntity);

    if (request.getAlbumId() != null) {
      final AlbumEntity albumEntity =
          this.albumEntityDao
              .findById(request.getAlbumId())
              .orElseThrow(() -> NapolloException.badRequest("Album is not available"));
      mediaEntity.setAlbum(albumEntity);
    }

    mediaEntity.setDescription(request.getDescription());
    mediaEntity.setShortId(RandomStringUtils.randomAlphanumeric(8));
    mediaEntity.setName(request.getTitle());
    mediaEntity.setCreatedUser(this.permissionManager.getAuthorizedUser(authorization).getId());
    mediaEntity.setFeaturedArtists(request.getFeaturedArtists());
    mediaEntity.setAccountUser(
        this.accountUserEntityDao
            .findByUser_EmailAddress(authorizedUserEntity.getEmailAddress())
            .orElseThrow(
                () ->
                    NapolloException.internalError(
                        "Unable to complete this operation. Please try again")));
    if (request.getTaggedAccountUsers() != null) {
      mediaEntity.setTaggedAccountUsers(
          request.getTaggedAccountUsers().stream()
              .map(this.accountUserEntityDao::findById)
              .map(Optional::get)
              .collect(Collectors.toList()));
    }

    // persist the media file
    if (request.getMediaMultipartFile() != null && !request.getMediaMultipartFile().isEmpty()) {
      try {
        mediaTypeValidator.validate(request.getMediaMultipartFile());
        final String mediaName = RandomStringUtils.randomAlphanumeric(64);
        final String fileExtensionName =
            Files.getFileExtension(request.getMediaMultipartFile().getOriginalFilename());
        request
            .getMediaMultipartFile()
            .transferTo(
                Paths.get(
                    mediaFilePath
                        .concat("/")
                        .concat(mediaName)
                        .concat(".")
                        .concat(fileExtensionName)));
        mediaEntity.setMediaUrl(mediaName.concat(".").concat(fileExtensionName));
		String fileName = mediaFilePath.concat("/").concat(mediaName).concat(".").concat(fileExtensionName);
		if ("mp4".equals(fileExtensionName)) {
			mediaEntity.setDuration(getMp4MediaDuration(Paths.get(fileName)));
		} else {
			mediaEntity.setDuration(getMediaDuration(Paths.get(fileName)));
		}
      } catch (IOException ioException) {
        this.loggerManager.log(
            MediaManagerBean.class, LogLevel.ERROR, ioException.getMessage(), ioException);
        throw NapolloException.internalError("Unable to process the media file");
      }
    }

    // persist the media trailer
    if (request.getTrailerMultipartFile() != null && !request.getTrailerMultipartFile().isEmpty()) {
      try {
        mediaTypeValidator.validate(request.getTrailerMultipartFile());
        final String mediaName = RandomStringUtils.randomAlphanumeric(64);
        final String fileExtensionName =
            Files.getFileExtension(request.getTrailerMultipartFile().getOriginalFilename());
        request
            .getTrailerMultipartFile()
            .transferTo(
                Paths.get(
                    mediaFilePath
                        .concat("/")
                        .concat(mediaName)
                        .concat(".")
                        .concat(fileExtensionName)));
        mediaEntity.setTrailerUrl(mediaName.concat(".").concat(fileExtensionName));
		String fileName = mediaFilePath.concat("/").concat(mediaName).concat(".").concat(fileExtensionName);
		if ("mp4".equals(fileExtensionName)) {
			mediaEntity.setDuration(getMp4MediaDuration(Paths.get(fileName)));
		} else {
			mediaEntity.setDuration(getMediaDuration(Paths.get(fileName)));
		}
      } catch (IOException ioException) {
        this.loggerManager.log(
            MediaManagerBean.class, LogLevel.ERROR, ioException.getMessage(), ioException);
        throw NapolloException.internalError("Unable to process the media trailer file");
      }
    }

    // persist the media photo
    if (request.getPhotoMultipartFile() != null && !request.getPhotoMultipartFile().isEmpty()) {
      try {
        imageTypeValidator.validate(request.getPhotoMultipartFile());
        final String mediaName = RandomStringUtils.randomAlphanumeric(64);
        final String fileExtensionName =
            Files.getFileExtension(request.getPhotoMultipartFile().getOriginalFilename());
        request
            .getPhotoMultipartFile()
            .transferTo(
                Paths.get(
                    mediaImagePath
                        .concat("/")
                        .concat(mediaName)
                        .concat(".")
                        .concat(fileExtensionName)));
        mediaEntity.setPhotoUrl(mediaName.concat(".").concat(fileExtensionName));
      } catch (IOException ioException) {
        this.loggerManager.log(
            MediaManagerBean.class, LogLevel.ERROR, ioException.getMessage(), ioException);
        throw NapolloException.internalError("Unable to process the media image file");
      }
    }

    // persist the media
    mediaEntity.setComments(0);
    mediaEntity.setHits(0);
    mediaEntity.setLikes(0);
    mediaEntity.setAccountUserLikes(new ArrayList<>());
    this.mediaEntityDao.save(mediaEntity);

    final ActivityRequest activityRequest = new ActivityRequest();
    activityRequest.setActivityType(ActivityType.CREATE_MEDIA);
    activityRequest.setActivitySourceOwnerId(mediaEntity.getId());
    activityRequest.setActivityDestinationOwnerId(mediaEntity.getId());
    activityRequest.setActivityUserId(permissionManager.getAuthorizedUser(authorization).getId());
    activityManager.postActivity(authorization, activityRequest);

    return AbstractResponse.response(
        this.mediaConverter.createResponse(mediaEntity), "Media saved successfully", Boolean.TRUE);
  }

  @Override
  public AbstractResponse<Media> get(Authorization authorization, String identity)
      throws NapolloException {
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.GET_MEDIA.name()));
    final MediaEntity mediaEntity =
        this.mediaEntityDao
            .findById(identity)
            .orElseThrow(() -> NapolloException.badRequest("Media is not available"));
    return AbstractResponse.response(
        this.mediaConverter
            .createResponse(mediaEntity)
            .orElseThrow(() -> NapolloException.internalError("Unable to complete this operation")),
        "Media information fetched successfully",
        Boolean.TRUE);
  }

  @Override
  public AbstractResponse<Page<Media>> list(Authorization authorization, Integer page, Integer size)
      throws NapolloException {
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.GET_MEDIAS.name()));
    final UserEntity authorizedUserEntity = this.permissionManager.getAuthorizedUser(authorization);
    Page<MediaEntity> mediaEntityPage = null;
    switch (authorizedUserEntity.getProfile().getProfileType()) {
      case "SUPERADMINISTRATOR":
        mediaEntityPage = this.mediaEntityDao.findAll(PageRequest.of(page, size));
        break;
      case "ARTIST":
        mediaEntityPage =
            this.mediaEntityDao
                .findByAccountUser_User_Id(authorizedUserEntity.getId(), PageRequest.of(page, size))
                .orElseThrow(() -> NapolloException.internalError("No media found for this user"));
        break;
      default:
        throw NapolloException.internalError("Invalid access. Please contact administrator");
    }
    final Page<Media> mediaPage =
        new PageImpl(
            mediaEntityPage.stream()
                .map(this.mediaConverter::createResponse)
                .collect(Collectors.toList()),
            mediaEntityPage.getPageable(),
            mediaEntityPage.getTotalElements());
    return AbstractResponse.response(mediaPage, "Media listed successfully", Boolean.TRUE);
  }

  @Override
  @Transactional
  public AbstractResponse update(Authorization authorization, String identity, MediaRequest request)
      throws NapolloException {
    return null;
  }

  @Override
  @Transactional
  public AbstractResponse delete(Authorization authorization, String identity)
      throws NapolloException {
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.DELETE_MEDIA.name()));
    final MediaEntity mediaEntity =
        this.mediaEntityDao
            .findById(identity)
            .orElseThrow(() -> NapolloException.badRequest("Media is not available"));
    final UserEntity authorizedUserEntity = this.permissionManager.getAuthorizedUser(authorization);
    if (!mediaEntity.getAccountUser().getUser().getId().equals(authorizedUserEntity.getId())
        && this.permissionManager.isAccountUser(authorization)) {
      throw NapolloException.internalError(
          "You cannot delete this media. Please make sure you are the media owner");
    }
    this.commentEntityDao.deleteAll(mediaEntity.getMediaComments());
    this.playEntityDao.deleteAll(this.playEntityDao.findByMedia_Id(identity));
    this.playHistoryEntityDao.deleteAll(this.playHistoryEntityDao.findByMedia_Id(identity));
    try {
      java.nio.file.Files.deleteIfExists(
          Paths.get(mediaFilePath.concat("/").concat(mediaEntity.getMediaUrl())));
      java.nio.file.Files.deleteIfExists(
          Paths.get(mediaFilePath.concat("/").concat(mediaEntity.getTrailerUrl())));
      java.nio.file.Files.deleteIfExists(
          Paths.get(mediaImagePath.concat("/").concat(mediaEntity.getPhotoUrl())));
    } catch (IOException ioException) {
      throw NapolloException.internalError("Unable to remove media and image files");
    }
    this.mediaEntityDao.delete(mediaEntity);
    return AbstractResponse.response("Media removed successfully", Boolean.TRUE);
  }

  @Override
  @Transactional
  public AbstractResponse like(Authorization authorization, String mediaIdentity, Boolean likeState)
      throws NapolloException {
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.LIKE_MEDIA.name()));
    final MediaEntity mediaEntity =
        this.mediaEntityDao
            .findById(mediaIdentity)
            .orElseThrow(() -> NapolloException.badRequest("Media is not available"));
    final AccountUserEntity accountUserEntity =
        this.accountUserEntityDao
            .findByUser_EmailAddress(
                this.permissionManager.getAuthorizedUser(authorization).getEmailAddress())
            .orElseThrow(() -> NapolloException.internalError("Unable to complete this operation"));
    if (mediaEntity.getAccountUserLikes() == null) {
      mediaEntity.setAccountUserLikes(new ArrayList<>());
    }
    if (mediaEntity.getLikes() == null) {
      mediaEntity.setLikes(0);
    }
    if (likeState) {
      if (!mediaEntity.getAccountUserLikes().contains(accountUserEntity)) {
        mediaEntity.getAccountUserLikes().add(accountUserEntity);
        mediaEntity.setLikes(mediaEntity.getLikes() + 1);
      } else {
        throw NapolloException.internalError("Media likes already registered");
      }
    } else {
      if (mediaEntity.getAccountUserLikes().contains(accountUserEntity)) {
        mediaEntity.getAccountUserLikes().remove(accountUserEntity);
        mediaEntity.setLikes(mediaEntity.getLikes() - 1);
      }
    }
    this.mediaEntityDao.save(mediaEntity);

    final ActivityRequest activityRequest = new ActivityRequest();
    activityRequest.setActivityType(ActivityType.LIKE_MEDIA);
    activityRequest.setActivitySourceOwnerId(mediaEntity.getId());
    activityRequest.setActivityDestinationOwnerId(mediaEntity.getId());
    activityRequest.setActivityUserId(permissionManager.getAuthorizedUser(authorization).getId());
    activityManager.postActivity(authorization, activityRequest);

    return AbstractResponse.response("Likes updated successfully", Boolean.TRUE);
  }

  @Override
  @Transactional
  public AbstractResponse play(
      Authorization authorization, String mediaIdentity, PlayRequest playRequest)
      throws NapolloException {
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.PLAY_MEDIA.name()));
    final String userIdentity = this.permissionManager.getAuthorizedUser(authorization).getId();
    final MediaEntity mediaEntity =
        this.mediaEntityDao
            .findById(mediaIdentity)
            .orElseThrow(() -> NapolloException.badRequest("Media is not available"));
    final PlayEntity playEntity =
        this.playEntityDao
            .findByMedia_IdAndCityAndStateAndCountry(
                mediaEntity.getId(),
                playRequest.getCity().toLowerCase(),
                playRequest.getState().toLowerCase(),
                playRequest.getCountry().toLowerCase())
            .orElse(new PlayEntity());
    playEntity.setMedia(mediaEntity);
    if (mediaEntity.getHits() == null) {
      mediaEntity.setHits(0);
    }
    mediaEntity.setHits(mediaEntity.getHits() + 1);
    playEntity.setHitCount(mediaEntity.getHits());
    playEntity.setCity(playRequest.getCity().toLowerCase());
    playEntity.setState(playRequest.getState().toLowerCase());
    playEntity.setCountry(playRequest.getCountry().toLowerCase());
    final Optional<PlayHistoryEntity> playHistoryEntityOptional =
        this.playHistoryEntityDao.findByMedia_IdAndAccountUser_User_Id(mediaIdentity, userIdentity);
    if (playHistoryEntityOptional.isPresent()) {
      final PlayHistoryEntity playHistoryEntity = playHistoryEntityOptional.get();
      playHistoryEntity.setPlays(playHistoryEntity.getPlays() + 1);
      playHistoryEntity.setHistoryDate(LocalDateTime.now().toDate());
      this.playHistoryEntityDao.save(playHistoryEntity);
    } else {
      final PlayHistoryEntity playHistoryEntity = new PlayHistoryEntity();
      playHistoryEntity.setHistoryDate(LocalDateTime.now().toDate());
      playHistoryEntity.setMedia(mediaEntity);
      playHistoryEntity.setPlays(1);
      playHistoryEntity.setAccountUser(
          this.accountUserEntityDao
              .findByUser_EmailAddress(
                  this.permissionManager.getAuthorizedUser(authorization).getEmailAddress())
              .orElseThrow(() -> NapolloException.internalError("Cannot complete this operation")));
      playHistoryEntity.setCreatedUser(
          this.permissionManager.getAuthorizedUser(authorization).getId());
      this.playHistoryEntityDao.save(playHistoryEntity);
    }
    this.mediaEntityDao.save(mediaEntity);
    this.playEntityDao.save(playEntity);
    return AbstractResponse.response("Media play registered successfully", Boolean.TRUE);
  }

  @Override
  public AbstractResponse<Page<AccountUser>> accountUserMediaLikes(
      Authorization authorization, String mediaIdentity) throws NapolloException {
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.GET_MEDIA_LIKES.name()));
    final MediaEntity mediaEntity =
        this.mediaEntityDao
            .findById(mediaIdentity)
            .orElseThrow(() -> NapolloException.badRequest("Media is not available"));
    final List<AccountUser> accountUsers =
        mediaEntity.getAccountUserLikes().stream()
            .map(this.accountUserConverter::createResponse)
            .map(Optional::get)
            .collect(Collectors.toList());
    final Page<AccountUser> accountUserPage =
        new PageImpl(
            accountUsers.subList(0, accountUsers.size() < 1000 ? accountUsers.size() : 1000));
    return AbstractResponse.response(
        accountUserPage, "Media likes listed successfully", Boolean.TRUE);
  }

  @Override
  public AbstractResponse<Page<Media>> accountUserMedia(
      Authorization authorization, Integer page, Integer size) throws NapolloException {
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.GET_MEDIAS.name()));
    final Page<MediaEntity> mediaEntityPage =
        this.mediaEntityDao
            .findByAccountUser_Id(
                this.permissionManager.getAuthorizedUser(authorization).getId(),
                PageRequest.of(page, size))
            .orElseThrow(
                () -> NapolloException.internalError("No media listed for this account user"));
    final Page<Media> mediaPage =
        new PageImpl(
            mediaEntityPage.stream()
                .map(this.mediaConverter::createResponse)
                .collect(Collectors.toList()),
            mediaEntityPage.getPageable(),
            mediaEntityPage.getTotalElements());
    return AbstractResponse.response(mediaPage, "Media listed successfully", Boolean.TRUE);
  }

  @Override
  public AbstractResponse<Page<Media>> accountUserMedia(
      Authorization authorization, String accountUserIdentity, Integer page, Integer size)
      throws NapolloException {
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.GET_MEDIAS.name()));
    final Page<MediaEntity> mediaEntityPage =
        this.mediaEntityDao
            .findByAccountUser_Id(accountUserIdentity, PageRequest.of(page, size))
            .orElseThrow(
                () -> NapolloException.internalError("No media listed for this account user"));
    final Page<Media> mediaPage =
        new PageImpl(
            mediaEntityPage.stream()
                .map(this.mediaConverter::createResponse)
                .collect(Collectors.toList()),
            mediaEntityPage.getPageable(),
            mediaEntityPage.getTotalElements());
    return AbstractResponse.response(mediaPage, "Media listed successfully", Boolean.TRUE);
  }

  @Override
  public AbstractResponse<Page<Media>> trendingMedia(
      Authorization authorization,
      String city,
      String state,
      String country,
      Integer page,
      Integer size)
      throws NapolloException {
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.GET_MEDIAS.name()));
    Page<PlayEntity> playEntityPage = null;
    if (city == null && state == null && country == null) {
      playEntityPage =
          this.playEntityDao.findAll(
              PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "hitCount")));
    } else {
      playEntityPage =
          this.playEntityDao
              .findByCityAndStateAndCountry(
                  city.toLowerCase(),
                  state.toLowerCase(),
                  country.toLowerCase(),
                  PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "hitCount")))
              .orElseThrow(() -> NapolloException.internalError("No trending media is available"));
    }
    final Page<Media> mediaPage =
        new PageImpl(
            playEntityPage.stream()
                .map(PlayEntity::getMedia)
                .map(this.mediaConverter::createResponse)
                .collect(Collectors.toList()),
            playEntityPage.getPageable(),
            playEntityPage.getTotalElements());
    return AbstractResponse.response(mediaPage, "Media listed successfully", Boolean.TRUE);
  }

  @Override
  public AbstractResponse<Page<Play>> accountUserPlayHistory(
      Authorization authorization, Integer page, Integer size) throws NapolloException {
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.GET_MEDIAS.name()));
    final Page<PlayHistoryEntity> playHistoryEntityPage =
        this.playHistoryEntityDao
            .findByAccountUser_User_Id(
                this.permissionManager.getAuthorizedUser(authorization).getId(),
                PageRequest.of(page, size))
            .orElseThrow(() -> NapolloException.internalError("Play history is not available"));
    final Page<Play> playPage =
        new PageImpl(
            playHistoryEntityPage.stream()
                .map(this.playHistoryConverter::createResponse)
                .collect(Collectors.toList()),
            playHistoryEntityPage.getPageable(),
            playHistoryEntityPage.getTotalElements());
    return AbstractResponse.response(playPage, "Play history fetched successfully", Boolean.TRUE);
  }

  @Override
  public AbstractResponse<Page<AccountUser>> trendingAccountUser(
      Authorization authorization,
      String city,
      String state,
      String country,
      Integer page,
      Integer size)
      throws NapolloException {
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.GET_MEDIAS.name()));
    final Page<PlayEntity> playEntityPage =
        this.playEntityDao
            .findByCityAndStateAndCountry(
                city.toLowerCase(),
                state.toLowerCase(),
                country.toLowerCase(),
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "hitCount")))
            .orElseThrow(() -> NapolloException.internalError("No trending artists is available"));
    final Page<AccountUser> accountUserPage =
        new PageImpl(
            playEntityPage.stream()
                .map(PlayEntity::getMedia)
                .map(MediaEntity::getAccountUser)
                .map(this.accountUserConverter::createResponse)
                .map(Optional::get)
                .collect(Collectors.toList()),
            playEntityPage.getPageable(),
            playEntityPage.getTotalElements());
    return AbstractResponse.response(
        accountUserPage, "Trending account user listed successfully", Boolean.TRUE);
  }

  @Override
  @Transactional
  public AbstractResponse markAsDiscovered(Authorization authorization, String mediaIdentity)
      throws NapolloException {
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.CREATE_MEDIA.name()));
    final MediaEntity mediaEntity =
        this.mediaEntityDao
            .findById(mediaIdentity)
            .orElseThrow(() -> NapolloException.badRequest("Media is not available"));
    final AccountUserEntity accountUserEntity =
        this.accountUserEntityDao
            .findByUser_EmailAddress(
                this.permissionManager.getAuthorizedUser(authorization).getEmailAddress())
            .orElseThrow(() -> NapolloException.internalError("Account user is not available"));
    if (!mediaEntity.getAccountUser().equals(accountUserEntity)) {
      throw NapolloException.internalError("Invalid access to this operation");
    }
    final MediaEntity discoveredMediaEntity =
        this.mediaEntityDao
            .findByAccountUser_User_IdAndDiscoveredTrue(accountUserEntity.getUser().getId())
            .orElse(null);
    if (discoveredMediaEntity != null) {
      discoveredMediaEntity.setDiscovered(Boolean.FALSE);
      this.mediaEntityDao.save(discoveredMediaEntity);
    }
    mediaEntity.setDiscovered(Boolean.TRUE);
    this.mediaEntityDao.save(mediaEntity);
    return AbstractResponse.response("Media marked successfully", Boolean.TRUE);
  }

  @Override
  public AbstractResponse<Page<Media>> discoveredMedia(
      Authorization authorization, Integer page, Integer size) throws NapolloException {
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.GET_MEDIAS.name()));
    final Page<MediaEntity> mediaEntityPage =
        this.mediaEntityDao.findByDiscoveredTrue(
            PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "likes")));
    final Page<Media> mediaPage =
        new PageImpl(
            mediaEntityPage.stream()
                .map(this.mediaConverter::createResponse)
                .collect(Collectors.toList()),
            mediaEntityPage.getPageable(),
            mediaEntityPage.getTotalElements());
    return AbstractResponse.response(
        mediaPage, "Discovered media listed successfully", Boolean.TRUE);
  }

  /**
   * Like a discovered media
   *
   * @param authorization
   * @param mediaIdentity
   * @param likeStatus
   * @return
   * @throws NapolloException
   */
  @Override
  @Transactional
  public AbstractResponse likeDiscoveredMedia(
      final Authorization authorization, final String mediaIdentity, final Boolean likeStatus)
      throws NapolloException {
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.LIKE_DISCOVERED_MEDIA.name()));
    final AccountUserEntity accountUserEntity =
        this.accountUserEntityDao
            .findByUser_EmailAddress(
                this.permissionManager.getAuthorizedUser(authorization).getEmailAddress())
            .orElseThrow(() -> NapolloException.internalError("Unable to find user"));
    final MediaEntity discoveredMediaEntity =
        this.mediaEntityDao
            .findById(mediaIdentity)
            .orElseThrow(() -> NapolloException.internalError("Media is not available"));
    if (!discoveredMediaEntity.getDiscovered()) {
      throw NapolloException.internalError("Media is not marked as discovered. Please try again");
    }
    if (likeStatus) {
      discoveredMediaEntity.setLikes(discoveredMediaEntity.getLikes() + 1);
      mediaEntityDao.save(discoveredMediaEntity);
      accountUserEntity.getDiscoveredMedia().add(discoveredMediaEntity);
      accountUserEntityDao.save(accountUserEntity);
    } else {
      discoveredMediaEntity.setLikes(discoveredMediaEntity.getLikes() - 1);
      mediaEntityDao.save(discoveredMediaEntity);
      accountUserEntity.getDiscoveredMedia().remove(discoveredMediaEntity);
      accountUserEntityDao.save(accountUserEntity);
    }
    return AbstractResponse.response("Discovered media liked successfully", Boolean.TRUE);
  }

  @Override
  public AbstractResponse<List<Media>> likedDiscoveredMedia(Authorization authorization)
      throws NapolloException {
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.GET_LIKE_DISCOVERED_MEDIA.name()));
    final AccountUserEntity accountUserEntity =
        this.accountUserEntityDao
            .findByUser_EmailAddress(
                this.permissionManager.getAuthorizedUser(authorization).getEmailAddress())
            .orElseThrow(() -> NapolloException.internalError("Unable to find user"));
    final List<MediaEntity> mediaEntities = accountUserEntity.getDiscoveredMedia();
    final List<Media> media =
        mediaEntities.stream()
            .map(mediaConverter::createResponse)
            .map(Optional::get)
            .collect(Collectors.toList());
    return AbstractResponse.response(media, "Discovered media listed successfully", Boolean.TRUE);
  }

  @Override
  public AbstractResponse<Page<Media>> search(
      Authorization authorization, MediaSearchRequest mediaSearchRequest) throws NapolloException {
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.GET_MEDIAS.name()));
    Boolean joinStatus = false;
    final StringBuilder queryBuilder = new StringBuilder("select m from MediaEntity m ");
    if (mediaSearchRequest.getCountryName() != null
        || mediaSearchRequest.getAlbumName() != null
        || mediaSearchRequest.getGenreName() != null
        || mediaSearchRequest.getArtistName() != null
        || mediaSearchRequest.getTitle() != null) {
      queryBuilder.append("where ");
    }
    if (mediaSearchRequest.getTitle() != null && !mediaSearchRequest.getTitle().isEmpty()) {
    	queryBuilder.append("lower(m.name) like '%").append(mediaSearchRequest.getTitle().toLowerCase()).append("%'");
      joinStatus = true;
    }
    if (mediaSearchRequest.getAlbumName() != null && !mediaSearchRequest.getAlbumName().isEmpty()) {
      if (joinStatus) {
        queryBuilder.append("or ");
      }
      queryBuilder
          .append(" lower(m.album.name) like '%")
          .append(mediaSearchRequest.getAlbumName().toLowerCase())
          .append("%'");
      joinStatus = true;
    }
    if (mediaSearchRequest.getGenreName() != null && !mediaSearchRequest.getGenreName().isEmpty()) {
      if (joinStatus) {
        queryBuilder.append("or ");
      }
      queryBuilder
          .append("lower( m.genre.name ) like '%")
          .append(mediaSearchRequest.getGenreName().toLowerCase())
          .append("%'");
      joinStatus = true;
    }
    if (mediaSearchRequest.getArtistName() != null
        && !mediaSearchRequest.getArtistName().isEmpty()) {
      if (joinStatus) {
        queryBuilder.append("or ");
      }
      queryBuilder
          .append(" lower(m.accountUser.firstName) like '%")
          .append(mediaSearchRequest.getArtistName().toLowerCase())
          .append("%'");
      queryBuilder
          .append(" or lower(m.accountUser.lastName) like '%")
          .append(mediaSearchRequest.getArtistName().toLowerCase())
          .append("%'");
    }
    if (mediaSearchRequest.getSortByDate()) {
      queryBuilder.append(" order by m.timestamp desc");
    }
    final List<MediaEntity> mediaEntityPage =
        this.entityManager
            .createQuery(queryBuilder.toString(), MediaEntity.class)
            .setFirstResult(mediaSearchRequest.getPage() * mediaSearchRequest.getSize())
            .setMaxResults(mediaSearchRequest.getSize())
            .getResultList();
    final Page<Media> mediaPage =
        new PageImpl(
            mediaEntityPage.stream()
                .map(this.mediaConverter::createResponse)
                .collect(Collectors.toList()));
    return AbstractResponse.response(mediaPage, "Media listed successfully", Boolean.TRUE);
  }

  private Float getMediaDuration(final Path path) throws NapolloException {
    try {
      final AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(path.toFile());
      final AudioFormat audioFormat = audioInputStream.getFormat();
      final Float duration =
          path.toFile().length() / (audioFormat.getFrameSize() * audioFormat.getFrameRate());
      return duration;
    } catch (Exception exception) {
      this.loggerManager.log(
          MediaManagerBean.class, LogLevel.ERROR, exception.getMessage(), exception);
      return Float.valueOf("0");
    }
  }

  private String likeQuery(String param) {
    return "%".concat(param).concat("%");
  }
  
	private Float getMp4MediaDuration(Path path) throws IOException {
		double DurationVideo = 0;
		IsoFile isoFile = new IsoFile(path.toFile());
		DurationVideo = (double) isoFile.getMovieBox().getMovieHeaderBox().getDuration()
				/ isoFile.getMovieBox().getMovieHeaderBox().getTimescale();
		return (float) DurationVideo;
	}
}
