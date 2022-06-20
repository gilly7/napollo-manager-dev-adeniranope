package com.wutanda.napollo.managers.accountusers.beans;

import com.google.common.collect.ImmutableMap;
import com.google.common.io.Files;
import com.wutanda.napollo.api.v1_0.accountusers.AccountUserRequest;
import com.wutanda.napollo.api.v1_0.accountusers.EmailActivationRequest;
import com.wutanda.napollo.api.v1_0.accountusers.MsisdnActivationRequest;
import com.wutanda.napollo.api.v1_0.accountusers.common.AccountUser;
import com.wutanda.napollo.api.v1_0.notifications.NotificationRequest;
import com.wutanda.napollo.common.activity.ActivityRequest;
import com.wutanda.napollo.common.activity.ActivityType;
import com.wutanda.napollo.common.authorization.Authorization;
import com.wutanda.napollo.common.authorization.PermissionRegistry;
import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.common.loggers.LogLevel;
import com.wutanda.napollo.common.transport.AbstractResponse;
import com.wutanda.napollo.converters.accountusers.AccountUserConverter;
import com.wutanda.napollo.managers.accountusers.AccountUserManager;
import com.wutanda.napollo.managers.activity.ActivityManager;
import com.wutanda.napollo.managers.authorization.PermissionManager;
import com.wutanda.napollo.managers.loggers.LoggerManager;
import com.wutanda.napollo.managers.notification.NotificationManager;
import com.wutanda.napollo.persistence.accountusers.AccountUserEntity;
import com.wutanda.napollo.persistence.accountusers.AccountUserUpgradeRequestEntity;
import com.wutanda.napollo.persistence.accountusers.dao.AccountUserEntityDao;
import com.wutanda.napollo.persistence.accountusers.dao.AccountUserUpgradeRequestEntityDao;
import com.wutanda.napollo.persistence.authorization.ProfileEntity;
import com.wutanda.napollo.persistence.authorization.dao.ProfileEntityDao;
import com.wutanda.napollo.persistence.music.GenreEntity;
import com.wutanda.napollo.persistence.music.dao.GenreEntityDao;
import com.wutanda.napollo.persistence.users.UserEntity;
import com.wutanda.napollo.persistence.users.dao.UserEntityDao;
import com.wutanda.napollo.validators.common.EmailAddressValidator;
import com.wutanda.napollo.validators.common.ImageTypeValidator;
import com.wutanda.napollo.validators.common.URLValidator;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.format.DateTimeFormat;
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
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class AccountUserManagerBean implements AccountUserManager {

  @Autowired private AccountUserEntityDao accountUserEntityDao;

  @Autowired private UserEntityDao userEntityDao;

  @Autowired private ProfileEntityDao profileEntityDao;

  @Autowired private AccountUserConverter accountUserConverter;

  @Autowired private PermissionManager permissionManager;

  @Autowired private LoggerManager loggerManager;

  @Autowired private GenreEntityDao genreEntityDao;

  @Autowired private AccountUserUpgradeRequestEntityDao accountUserUpgradeRequestEntityDao;

  @Autowired private NotificationManager notificationManager;

  @Autowired private EmailAddressValidator emailAddressValidator;

  @Autowired private ImageTypeValidator imageTypeValidator;

  @Autowired private ActivityManager activityManager;

  @Autowired private URLValidator urlValidator;

  @Value("${napollo.registration.profile.name}")
  private String registrationAccountUserProfile;

  @Value("${napollo.activation.code.length}")
  private String activationCodeLength;

  @Value("${napollo.media.image.path}")
  private String mediaImagePath;

  @Override
  @Transactional
  public AbstractResponse create(Authorization authorization, AccountUserRequest request)
      throws NapolloException {
    this.loggerManager.log(
        AccountUserManagerBean.class, LogLevel.TRACE, "Creating a new account user");
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.CREATE_ACCOUNT_USER.name()));
    permissionManager.validateAdministratorAccess(authorization);
    final UserEntity authorizedUserEntity = this.permissionManager.getAuthorizedUser(authorization);

    if (request.getUsername() != null
        && this.accountUserEntityDao.findByUser_Username(request.getUsername()).isPresent()) {
      throw NapolloException.badRequest("Username is already registered");
    }

    if (request.getEmailAddress() != null
        && this.accountUserEntityDao
            .findByUser_EmailAddress(request.getEmailAddress())
            .isPresent()) {
      throw NapolloException.badRequest("Email address is already registered");
    }

    if (request.getMobileNumber() != null
        && this.accountUserEntityDao
            .findByUser_MobileNumber(request.getMobileNumber())
            .isPresent()) {
      throw NapolloException.badRequest("Mobile number is already registered");
    }

    if (request.getEmailAddress() != null && !request.getEmailAddress().isEmpty()) {
      this.emailAddressValidator.validate(request.getEmailAddress());
    } else {
      throw NapolloException.badRequest("Email address is required");
    }

    if (request.getWebsite() != null && !request.getWebsite().isEmpty()) {
      this.urlValidator.validate(request.getWebsite());
    }

    final UserEntity userEntity = new UserEntity();
    final ProfileEntity profileEntity =
        this.profileEntityDao
            .findByName(registrationAccountUserProfile)
            .orElseThrow(
                () ->
                    NapolloException.internalError(
                        "Unable to complete registration. Please try again"));
    userEntity.setProfile(profileEntity);
    userEntity.setUsername(request.getUsername());
    userEntity.setAuthorizationStatus("CREATED");
    userEntity.setPassword(request.getPassword());
    userEntity.setEmailAddress(request.getEmailAddress());
    userEntity.setEmailActivationToken(
        RandomStringUtils.randomNumeric(Integer.parseInt(activationCodeLength)));
    userEntity.setMobileNumber(request.getMobileNumber());
    userEntity.setMsisdnActivationToken(
        RandomStringUtils.randomNumeric(Integer.parseInt(activationCodeLength)));
    userEntity.setCreatedUser(authorizedUserEntity.getId());
    userEntity.setMsisdnActivationStatus(Boolean.FALSE);
    userEntity.setEmailActivationStatus(Boolean.FALSE);

    final AccountUserEntity accountUserEntity = new AccountUserEntity();

    accountUserEntity.setAccountUserType(profileEntity.getProfileType());
    accountUserEntity.setCountryCode(request.getCountryCode());
    accountUserEntity.setFirstName(request.getFirstName());
    accountUserEntity.setLastName(request.getLastName());
    accountUserEntity.setWebsite(request.getWebsite());
    try {
      Date dateOfBirth =
          DateTimeFormat.forPattern("yyyy-MM-dd").parseLocalDate(request.getDateOfBirth()).toDate();
      accountUserEntity.setDateOfBirth(dateOfBirth);
    } catch (Exception ex) {
      throw NapolloException.badRequest(
          "Invalid date of birth. Date of birth should be of format yyyy-MM-dd");
    }
    accountUserEntity.setCreatedUser(authorizedUserEntity.getId());
    accountUserEntity.setProvince(request.getState());
    accountUserEntity.setUser(userEntityDao.save(userEntity));
    this.accountUserEntityDao.save(accountUserEntity);

    sendEmailActivationCode(userEntity);
    sendMsisdnActivationCode(userEntity);

    return AbstractResponse.response(
        "Registration successful. Please check your mail to continue registration", Boolean.TRUE);
  }

  @Override
  public AbstractResponse<AccountUser> get(Authorization authorization, String identity)
      throws NapolloException {
    this.loggerManager.log(
        AccountUserManagerBean.class,
        LogLevel.TRACE,
        "Getting an account user with ID ".concat(identity));
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.GET_ACCOUNT_USER.name()));
    permissionManager.validateAdministratorAccess(authorization);
    final AccountUserEntity persistedAccountUserEntity =
        this.accountUserEntityDao
            .findById(identity)
            .orElseThrow(
                () ->
                    NapolloException.internalError("Account user is not found. Please try again"));
    return AbstractResponse.response(
        this.accountUserConverter.createResponse(persistedAccountUserEntity),
        "Account user fetched successfully",
        Boolean.TRUE);
  }

  @Override
  public AbstractResponse<Page<AccountUser>> list(
      Authorization authorization, Integer page, Integer size) throws NapolloException {
    this.loggerManager.log(
        AccountUserManagerBean.class, LogLevel.TRACE, "Listing all account users");
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.GET_ACCOUNT_USERS.name()));
    permissionManager.validateAdministratorAccess(authorization);
    final Page<AccountUserEntity> accountUserEntityPage =
        this.accountUserEntityDao.findAll(PageRequest.of(page, size));
    final Page<AccountUser> accountUserPage =
        new PageImpl(
            accountUserEntityPage.stream()
                .map(this.accountUserConverter::createResponse)
                .collect(Collectors.toList()),
            accountUserEntityPage.getPageable(),
            accountUserEntityPage.getTotalElements());
    return AbstractResponse.response(
        accountUserPage, "Account users listed successfully", Boolean.TRUE);
  }

  @Override
  @Transactional
  public AbstractResponse update(
      Authorization authorization, String identity, AccountUserRequest request)
      throws NapolloException {
    this.loggerManager.log(
        AccountUserManagerBean.class,
        LogLevel.TRACE,
        "Updating account request with ID : ".concat(identity));
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.UPDATE_ACCOUNT_USER.name()));
    permissionManager.validateAdministratorAccess(authorization);
    final AccountUserEntity persistedAccountUserEntity =
        this.accountUserEntityDao
            .findById(identity)
            .orElseThrow(
                () ->
                    NapolloException.internalError("Account user is not found. Please try again"));
    if (request.getWebsite() != null) {
      persistedAccountUserEntity.setWebsite(request.getWebsite());
    }
    if (request.getLastName() != null) {
      persistedAccountUserEntity.setLastName(request.getLastName());
    }
    if (request.getCountryCode() != null) {
      persistedAccountUserEntity.setCountryCode(request.getCountryCode());
    }
    if (request.getDateOfBirth() != null) {
      try {
        Date dateOfBirth =
            DateTimeFormat.forPattern("yyyy-MM-dd")
                .parseLocalDate(request.getDateOfBirth())
                .toDate();
        persistedAccountUserEntity.setDateOfBirth(dateOfBirth);
      } catch (Exception ex) {
        throw NapolloException.badRequest(
            "Invalid date of birth. Date of birth should be of format yyyy-MM-dd");
      }
    }
    if (request.getStageName() != null
        && persistedAccountUserEntity.getUser().getProfile().getProfileType().equals("ARTIST")) {
      persistedAccountUserEntity.setStageName(request.getStageName());
    }
    if (request.getLastName() != null) {
      persistedAccountUserEntity.setLastName(request.getLastName());
    }
    if (request.getFirstName() != null) {
      persistedAccountUserEntity.setFirstName(request.getFirstName());
    }
    if (request.getCountryCode() != null) {
      persistedAccountUserEntity.setCountryCode(request.getCountryCode());
    }
    if (request.getState() != null) {
      persistedAccountUserEntity.setProvince(request.getState());
    }
    this.accountUserEntityDao.save(persistedAccountUserEntity);
    return AbstractResponse.response("Account user information updated successfully", Boolean.TRUE);
  }

  @Override
  @Transactional
  public AbstractResponse delete(Authorization authorization, String identity)
      throws NapolloException {
    this.loggerManager.log(
        AccountUserManagerBean.class,
        LogLevel.TRACE,
        "Removing account request with ID : ".concat(identity));
    permissionManager.validateAdministratorAccess(authorization);
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.UPDATE_ACCOUNT_USER.name()));
    final AccountUserEntity persistedAccountUserEntity =
        this.accountUserEntityDao
            .findById(identity)
            .orElseThrow(
                () ->
                    NapolloException.internalError("Account user is not found. Please try again"));
    this.userEntityDao.delete(persistedAccountUserEntity.getUser());
    this.accountUserEntityDao.delete(persistedAccountUserEntity);
    return AbstractResponse.response("Account user information removed successfully", Boolean.TRUE);
  }

  @Override
  @Transactional
  public AbstractResponse upgradeAccountUser(
      Authorization authorization, String accountUserIdentity, final String accountUserType)
      throws NapolloException {
    this.loggerManager.log(
        AccountUserManagerBean.class,
        LogLevel.TRACE,
        "Requesting for an upgrade to another profile");
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.UPGRADE_ACCOUNT_USER_REQUEST.name()));
    if (!this.accountUserUpgradeRequestEntityDao
        .findByAccountUser_Id(accountUserIdentity)
        .isPresent()) {
      final AccountUserEntity persistedAccountUserEntity =
          this.accountUserEntityDao
              .findById(accountUserIdentity)
              .orElseThrow(
                  () ->
                      NapolloException.internalError(
                          "Account user is not found. Please try again"));
      if (persistedAccountUserEntity.getAccountUserType().equals(accountUserType)) {
        throw NapolloException.internalError("Account upgrade already completed");
      }
      final AccountUserUpgradeRequestEntity accountUserUpgradeRequestEntity =
          new AccountUserUpgradeRequestEntity();
      accountUserUpgradeRequestEntity.setAccountUser(persistedAccountUserEntity);
      accountUserUpgradeRequestEntity.setAccountUserType(accountUserType);
      accountUserUpgradeRequestEntity.setFromProfile(
          persistedAccountUserEntity.getUser().getProfile());
      accountUserUpgradeRequestEntity.setPendingApproval(Boolean.FALSE);
      this.accountUserUpgradeRequestEntityDao.save(accountUserUpgradeRequestEntity);
      return AbstractResponse.response("Account upgrade saved successfully", Boolean.TRUE);
    } else {
      return AbstractResponse.response("Account upgrade already submitted", Boolean.FALSE);
    }
  }

  @Override
  @Transactional
  public AbstractResponse approveUpgradeAccountUser(
      Authorization authorization,
      String accountUserIdentity,
      String profileIdentity,
      Boolean approvalStatus)
      throws NapolloException {
    this.loggerManager.log(
        AccountUserManagerBean.class,
        LogLevel.TRACE,
        "Approving upgrade account user with ID "
            .concat(accountUserIdentity)
            .concat(" with profile ")
            .concat(profileIdentity));
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.UPGRADE_ACCOUNT_USER.name()));
    permissionManager.validateAdministratorAccess(authorization);
    final AccountUserUpgradeRequestEntity accountUserUpgradeRequestEntity =
        this.accountUserUpgradeRequestEntityDao
            .findByAccountUser_Id(accountUserIdentity)
            .orElseThrow(
                () ->
                    NapolloException.internalError(
                        "Account user upgrades not found. Please try again"));
    if (approvalStatus) {
      final AccountUserEntity persistedAccountUserEntity =
          this.accountUserEntityDao
              .findById(accountUserIdentity)
              .orElseThrow(
                  () ->
                      NapolloException.internalError(
                          "Account user is not found. Please try again"));
      final ProfileEntity upgradeProfileEntity =
          this.profileEntityDao
              .findById(profileIdentity)
              .orElseThrow(() -> NapolloException.internalError("Profile not available"));
      if (upgradeProfileEntity
          .getProfileType()
          .equals(persistedAccountUserEntity.getAccountUserType())) {
        throw NapolloException.internalError("Profile type mismatch");
      }
      final UserEntity userEntity = persistedAccountUserEntity.getUser();
      userEntity.setProfile(upgradeProfileEntity);
      this.userEntityDao.save(userEntity);
      persistedAccountUserEntity.setAccountUserType(upgradeProfileEntity.getProfileType());
      this.accountUserEntityDao.save(persistedAccountUserEntity);
    }
    this.accountUserUpgradeRequestEntityDao.delete(accountUserUpgradeRequestEntity);
    return AbstractResponse.response("Upgrade completed successfully", Boolean.TRUE);
  }

  @Override
  public AbstractResponse<AccountUser> getAccountUser(Authorization authorization)
      throws NapolloException {
    this.loggerManager.log(AccountUserManagerBean.class, LogLevel.TRACE, "Getting an account user");
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.GET_AUTHORIZED_ACCOUNT_USER.name()));
    final UserEntity authorizedUserEntity = this.permissionManager.getAuthorizedUser(authorization);
    final AccountUserEntity accountUserEntity =
        this.accountUserEntityDao
            .findByUser_EmailAddress(authorizedUserEntity.getEmailAddress())
            .orElseThrow(
                () -> NapolloException.internalError("Unable to fetch account user information"));
    return AbstractResponse.response(
        this.accountUserConverter.createResponse(accountUserEntity),
        "Account user information fetched successfully",
        Boolean.TRUE);
  }

  @Override
  @Transactional
  public AbstractResponse updateAccountUser(
      Authorization authorization, AccountUserRequest accountUserRequest) throws NapolloException {
    this.loggerManager.log(
        AccountUserManagerBean.class, LogLevel.TRACE, "Updating authorised account request");
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.UPDATE_AUTHORIZED_ACCOUNT_USER.name()));
    final UserEntity authorizedUserEntity = this.permissionManager.getAuthorizedUser(authorization);
    permissionManager.validateAccountUserAccess(authorization);
    final AccountUserEntity persistedAccountUserEntity =
        this.accountUserEntityDao
            .findByUser_EmailAddress(authorizedUserEntity.getEmailAddress())
            .orElseThrow(
                () -> NapolloException.internalError("Unable to fetch account user information"));
    if (accountUserRequest.getWebsite() != null) {
      persistedAccountUserEntity.setWebsite(accountUserRequest.getWebsite());
    }
    if (accountUserRequest.getLastName() != null) {
      persistedAccountUserEntity.setLastName(accountUserRequest.getLastName());
    }
    if (accountUserRequest.getCountryCode() != null) {
      persistedAccountUserEntity.setCountryCode(accountUserRequest.getCountryCode());
    }
    if (accountUserRequest.getDateOfBirth() != null) {
      try {
        Date dateOfBirth =
            DateTimeFormat.forPattern("yyyy-MM-dd")
                .parseLocalDate(accountUserRequest.getDateOfBirth())
                .toDate();
        persistedAccountUserEntity.setDateOfBirth(dateOfBirth);
      } catch (Exception ex) {
        throw NapolloException.badRequest(
            "Invalid date of birth. Date of birth should be of format yyyy-MM-dd");
      }
    }
    if (accountUserRequest.getUsername() != null) {
      if (this.userEntityDao.findByUsername(accountUserRequest.getUsername()).isPresent()) {
        throw NapolloException.internalError("Username already registered");
      }
      authorizedUserEntity.setUsername(accountUserRequest.getUsername());
    }
    if (accountUserRequest.getDateOfBirth() != null) {
      try {
        Date dateOfBirth =
            DateTimeFormat.forPattern("yyyy-MM-dd")
                .parseLocalDate(accountUserRequest.getDateOfBirth())
                .toDate();
        persistedAccountUserEntity.setDateOfBirth(dateOfBirth);
      } catch (Exception ex) {
        throw NapolloException.badRequest(
            "Invalid date of birth. Date of birth should be of format yyyy-MM-dd");
      }
    }
    if (accountUserRequest.getStageName() != null
        && persistedAccountUserEntity.getUser().getProfile().getProfileType().equals("ARTIST")) {
      persistedAccountUserEntity.setStageName(accountUserRequest.getStageName());
    }
    if (accountUserRequest.getLastName() != null) {
      persistedAccountUserEntity.setLastName(accountUserRequest.getLastName());
    }
    if (accountUserRequest.getFirstName() != null) {
      persistedAccountUserEntity.setFirstName(accountUserRequest.getFirstName());
    }
    if (accountUserRequest.getCountryCode() != null) {
      persistedAccountUserEntity.setCountryCode(accountUserRequest.getCountryCode());
    }
    if (accountUserRequest.getState() != null) {
      persistedAccountUserEntity.setProvince(accountUserRequest.getState());
    }
    this.userEntityDao.save(authorizedUserEntity);
    this.accountUserEntityDao.save(persistedAccountUserEntity);
    return AbstractResponse.response("Account user information updated successfully", Boolean.TRUE);
  }

  @Override
  @Transactional
  public AbstractResponse emailActivation(
      Authorization authorization, EmailActivationRequest emailActivationRequest)
      throws NapolloException {
    this.loggerManager.log(
        AccountUserManagerBean.class,
        LogLevel.TRACE,
        "Activating email address with the activation code");
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.EMAIL_ADDRESS_ACTIVATION.name()));
    final UserEntity persistedUserEntity =
        this.userEntityDao
            .findByEmailAddressAndEmailActivationToken(
                emailActivationRequest.getEmailAddress(),
                emailActivationRequest.getActivationCode())
            .orElseThrow(
                () ->
                    NapolloException.internalError("Invalid activation code. Please check again"));
    persistedUserEntity.setEmailActivationToken(null);
    persistedUserEntity.setAuthorizationStatus("ACTIVE");
    persistedUserEntity.setEmailActivationStatus(Boolean.TRUE);
    this.userEntityDao.save(persistedUserEntity);
    final NotificationRequest notificationRequest = new NotificationRequest();
    notificationRequest.setName("EMAIL_ACTIVATION");
    notificationRequest.setType("EMAIL");
    notificationRequest.setDestination(persistedUserEntity.getEmailAddress());
    notificationRequest.setParameters(ImmutableMap.<String, String>builder().build());
    notificationRequest.setPlainMessage("Your account is now active. Thanks for using Napollo");
    this.notificationManager.sendNotification(notificationRequest);
    return AbstractResponse.response("Email activation completed successfully", Boolean.TRUE);
  }

  @Override
  @Transactional
  public AbstractResponse msisdnActivation(
      Authorization authorization, MsisdnActivationRequest msisdnActivationRequest)
      throws NapolloException {
    this.loggerManager.log(
        AccountUserManagerBean.class,
        LogLevel.TRACE,
        "Activating mobile number with the activation code");
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.MSISDN_ACTIVATION.name()));
    final UserEntity persistedUserEntity =
        this.userEntityDao
            .findByMobileNumberAndMsisdnActivationToken(
                msisdnActivationRequest.getMobileNumber(),
                msisdnActivationRequest.getActivationToken())
            .orElseThrow(
                () ->
                    NapolloException.internalError("Invalid activation code. Please check again"));
    persistedUserEntity.setMsisdnActivationToken(null);
    persistedUserEntity.setAuthorizationStatus("ACTIVE");
    persistedUserEntity.setMsisdnActivationStatus(Boolean.TRUE);
    this.userEntityDao.save(persistedUserEntity);
    final NotificationRequest notificationRequest = new NotificationRequest();
    notificationRequest.setName("MSISDN_ACTIVATION");
    notificationRequest.setType("SMS");
    notificationRequest.setDestination(persistedUserEntity.getMobileNumber());
    notificationRequest.setParameters(ImmutableMap.<String, String>builder().build());
    this.notificationManager.sendNotification(notificationRequest);
    return AbstractResponse.response(
        "Mobile number activation completed successfully", Boolean.TRUE);
  }

  @Override
  @Transactional
  public AbstractResponse resendEmailActivationCode(
      Authorization authorization, EmailActivationRequest emailActivationRequest)
      throws NapolloException {
    this.loggerManager.log(
        AccountUserManagerBean.class,
        LogLevel.TRACE,
        "Resending activation code to the email address");
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.RESEND_EMAIL_ACTIVATION_CODE.name()));
    final UserEntity persistedUserEntity =
        this.userEntityDao
            .findByEmailAddress(emailActivationRequest.getEmailAddress())
            .orElseThrow(() -> NapolloException.internalError("Email address is not registered"));
    if (persistedUserEntity.getEmailActivationToken() != null) {
      this.loggerManager.log(
          AccountUserManagerBean.class,
          LogLevel.TRACE,
          "Email Activation Code : ".concat(persistedUserEntity.getEmailActivationToken()));
      if (persistedUserEntity.getEmailActivationStatus()) {
        throw NapolloException.internalError("");
      }
      sendEmailActivationCode(persistedUserEntity);
      return AbstractResponse.response("Email activation token resent successfully", Boolean.TRUE);
    }
    return AbstractResponse.response("Account already activated", Boolean.TRUE);
  }

  @Override
  @Transactional
  public AbstractResponse resendMsisdnActivationCode(
      Authorization authorization, MsisdnActivationRequest msisdnActivationRequest)
      throws NapolloException {
    this.loggerManager.log(
        AccountUserManagerBean.class,
        LogLevel.TRACE,
        "Resending activation code to the mobile number");
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.RESEND_MSISDN_ACTIVATION_CODE.name()));
    final UserEntity persistedUserEntity =
        this.userEntityDao
            .findByMobileNumber(msisdnActivationRequest.getMobileNumber())
            .orElseThrow(() -> NapolloException.internalError("Mobile number is not registered"));
    if (persistedUserEntity.getMsisdnActivationToken() != null) {
      this.loggerManager.log(
          AccountUserManagerBean.class,
          LogLevel.TRACE,
          "Msisdn Activation Code : ".concat(persistedUserEntity.getMsisdnActivationToken()));
      sendMsisdnActivationCode(persistedUserEntity);
      return AbstractResponse.response("Mobile activation token resent successfully", Boolean.TRUE);
    }
    return AbstractResponse.response("Account already activated", Boolean.TRUE);
  }

  @Override
  @Transactional
  public AbstractResponse assignAccountUserGenre(
      Authorization authorization,
      String accountUserIdentity,
      String genreIdentity,
      Boolean assigned)
      throws NapolloException {
    this.loggerManager.log(
        AccountUserManagerBean.class, LogLevel.TRACE, "Assigning an account user genre");
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.ASSIGN_ACCOUNT_USER_GENRE.name()));
    final AccountUserEntity persistedAccountUserEntity =
        this.accountUserEntityDao
            .findById(accountUserIdentity)
            .orElseThrow(
                () ->
                    NapolloException.internalError("Account user is not found. Please try again"));
    if (!persistedAccountUserEntity.getAccountUserType().equals("ARTIST")) {
      throw NapolloException.internalError(
          "Only ARTISTS are allowed to assign genres. Please try again");
    }
    final GenreEntity genreEntity =
        this.genreEntityDao
            .findById(genreIdentity)
            .orElseThrow(() -> NapolloException.internalError("Genre information not available"));
    if (assigned) {
      if (persistedAccountUserEntity.getGenres().contains(genreEntity)) {
        throw NapolloException.internalError("Genre already assigned to this account user");
      }
      persistedAccountUserEntity.getGenres().add(genreEntity);
    } else {
      if (!persistedAccountUserEntity.getGenres().contains(genreEntity)) {
        throw NapolloException.internalError("Genre not assigned to this account user");
      }
      persistedAccountUserEntity.getGenres().remove(genreEntity);
    }
    this.accountUserEntityDao.save(persistedAccountUserEntity);
    return AbstractResponse.response("Genre assigned successfully", Boolean.TRUE);
  }

  @Override
  @Transactional
  public AbstractResponse followAccountUser(
      Authorization authorization, String followAccountUserIdentity, Boolean followState)
      throws NapolloException {
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.FOLLOW_ACCOUNT_USER.name()));
    final UserEntity authorizedUserEntity = this.permissionManager.getAuthorizedUser(authorization);
    permissionManager.validateAccountUserAccess(authorization);
    final AccountUserEntity authorizedAccountUserEntity =
        this.accountUserEntityDao
            .findByUser_EmailAddress(authorizedUserEntity.getEmailAddress())
            .orElseThrow(() -> NapolloException.internalError("Unable to complete this operation"));
    final AccountUserEntity followAccountUserEntity =
        this.accountUserEntityDao
            .findById(followAccountUserIdentity)
            .orElseThrow(() -> NapolloException.internalError("Unable to find the account user"));
    if (followAccountUserEntity.getFollowingCount() == null) {
      followAccountUserEntity.setFollowingCount(0);
    }
    if (followAccountUserEntity.getFollowerCount() == null) {
      followAccountUserEntity.setFollowerCount(0);
    }
    if (authorizedAccountUserEntity.getFollowingCount() == null) {
      authorizedAccountUserEntity.setFollowingCount(0);
    }
    if (authorizedAccountUserEntity.getFollowerCount() == null) {
      authorizedAccountUserEntity.setFollowerCount(0);
    }
    if (followState) {
      if (followAccountUserEntity.getFollowAccountUsers().contains(authorizedAccountUserEntity)) {
        throw NapolloException.internalError("Follow request already registered");
      }
      followAccountUserEntity.getFollowAccountUsers().add(authorizedAccountUserEntity);
      followAccountUserEntity.setFollowerCount(followAccountUserEntity.getFollowerCount() + 1);
      authorizedAccountUserEntity.getFollowingAccountUsers().add(followAccountUserEntity);
      authorizedAccountUserEntity.setFollowingCount(
          authorizedAccountUserEntity.getFollowingCount() + 1);
    } else {
      if (!authorizedAccountUserEntity
          .getFollowingAccountUsers()
          .contains(followAccountUserEntity)) {
        throw NapolloException.internalError("Follow request is not available");
      }
      followAccountUserEntity.getFollowAccountUsers().remove(authorizedAccountUserEntity);
      followAccountUserEntity.setFollowerCount(followAccountUserEntity.getFollowerCount() - 1);
      authorizedAccountUserEntity.getFollowingAccountUsers().remove(followAccountUserEntity);
      authorizedAccountUserEntity.setFollowingCount(
          authorizedAccountUserEntity.getFollowingCount() - 1);
    }
    this.accountUserEntityDao.saveAll(
        Arrays.asList(followAccountUserEntity, authorizedAccountUserEntity));
    final ActivityRequest activityRequest = new ActivityRequest();
    activityRequest.setActivityType(ActivityType.FOLLOW);
    activityRequest.setActivitySourceOwnerId(followAccountUserEntity.getUser().getId());
    activityRequest.setActivityUserId(authorizedAccountUserEntity.getUser().getId());
    activityManager.postActivity(authorization, activityRequest);
    return AbstractResponse.response("Follow request registered successfully", Boolean.TRUE);
  }

  @Override
  @Transactional
  public AbstractResponse updateProfilePicture(
      Authorization authorization, MultipartFile multipartFile) throws NapolloException {
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.UPDATE_ACCOUNT_USER.name()));
    permissionManager.validateAccountUserAccess(authorization);
    final UserEntity authorizedUserEntity = this.permissionManager.getAuthorizedUser(authorization);
    final AccountUserEntity accountUserEntity =
        this.accountUserEntityDao
            .findByUser_EmailAddress(authorizedUserEntity.getEmailAddress())
            .orElseThrow(() -> NapolloException.internalError("Unable to complete this operation"));
    if (multipartFile != null && !multipartFile.isEmpty()) {
      this.imageTypeValidator.validate(multipartFile);
      try {
        if (accountUserEntity.getProfileUrl() != null) {
          final Path mediaPath =
              Paths.get(mediaImagePath.concat("/").concat(accountUserEntity.getProfileUrl()));
          mediaPath.toFile().delete();
        }
        final String profilePictureName = RandomStringUtils.randomAlphanumeric(64);
        final String fileExtensionName =
            Files.getFileExtension(multipartFile.getOriginalFilename());
        multipartFile.transferTo(
            Paths.get(
                mediaImagePath
                    .concat("/")
                    .concat(profilePictureName)
                    .concat(".")
                    .concat(fileExtensionName)));
        accountUserEntity.setProfileUrl(profilePictureName.concat(".").concat(fileExtensionName));
      } catch (IOException ioException) {
        this.loggerManager.log(
            AccountUserManagerBean.class, LogLevel.ERROR, ioException.getMessage(), ioException);
        throw NapolloException.internalError("Unable to update profile picture. Please try again");
      }
    }
    this.accountUserEntityDao.save(accountUserEntity);
    return AbstractResponse.response("Profile picture updated successfully", Boolean.TRUE);
  }

  private void sendEmailActivationCode(final UserEntity userEntity) throws NapolloException {
    this.loggerManager.log(
        AccountUserManagerBean.class, LogLevel.TRACE, "Sending email activation code ");
    final NotificationRequest emailNotificationRequest = new NotificationRequest();
    emailNotificationRequest.setTitle("Confirm your account");
    emailNotificationRequest.setName("EMAIL_ACTIVATION_CODE");
    emailNotificationRequest.setType("EMAIL");
    emailNotificationRequest.setDestination(userEntity.getEmailAddress());
    emailNotificationRequest.setParameters(
        ImmutableMap.<String, String>builder()
            .put("ACTIVATION_CODE", userEntity.getEmailActivationToken())
            .build());
    emailNotificationRequest.setPlainMessage(
        "Your email activation code is ".concat(userEntity.getEmailActivationToken()));
    this.notificationManager.sendNotification(emailNotificationRequest);
  }

  private void sendMsisdnActivationCode(final UserEntity userEntity) throws NapolloException {
    this.loggerManager.log(
        AccountUserManagerBean.class, LogLevel.TRACE, "Sending msisdn activation code");
    final NotificationRequest msisdnNotificationRequest = new NotificationRequest();
    msisdnNotificationRequest.setTitle("Confirm your account");
    msisdnNotificationRequest.setName("MSISDN_ACTIVATION_CODE");
    msisdnNotificationRequest.setType("SMS");
    msisdnNotificationRequest.setDestination(userEntity.getMobileNumber());
    msisdnNotificationRequest.setParameters(
        ImmutableMap.<String, String>builder()
            .put("ACTIVATION_CODE", userEntity.getMsisdnActivationToken())
            .build());
    this.notificationManager.sendNotification(msisdnNotificationRequest);
  }
}
