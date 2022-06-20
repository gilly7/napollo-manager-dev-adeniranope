package com.wutanda.napollo.converters.accountusers.beans;

import com.wutanda.napollo.api.v1_0.accountusers.AccountUserRequest;
import com.wutanda.napollo.api.v1_0.accountusers.common.AccountUser;
import com.wutanda.napollo.api.v1_0.location.common.Location;
import com.wutanda.napollo.converters.accountusers.AccountUserConverter;
import com.wutanda.napollo.converters.authorization.ProfileConverter;
import com.wutanda.napollo.converters.music.GenreConverter;
import com.wutanda.napollo.persistence.accountusers.AccountUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AccountUserConverterBean implements AccountUserConverter {

  @Autowired private GenreConverter genreConverter;

  @Autowired private ProfileConverter profileConverter;

  @Value("${napollo.media.image.url}")
  private String mediaImageUrl;

  @Override
  public Optional<AccountUserEntity> createEntity(AccountUserRequest request) {
    return Optional.empty();
  }

  @Override
  public Optional<AccountUser> createResponse(AccountUserEntity entity) {
    if (entity != null) {
      final AccountUser accountUser = new AccountUser();
      accountUser.setAccountUserType(entity.getAccountUserType());
      accountUser.setCountryCode(entity.getCountryCode());
      accountUser.setUsername(entity.getUser().getUsername());
      accountUser.setEmailAddress(entity.getUser().getEmailAddress());
      accountUser.setFirstName(entity.getFirstName());
      accountUser.setLastName(entity.getLastName());
      accountUser.setWebsite(entity.getWebsite());
      accountUser.setMobileNumber(entity.getUser().getMobileNumber());
      accountUser.setEmailActivationStatus(entity.getUser().getEmailActivationStatus());
      accountUser.setMsisdnActivationStatus(entity.getUser().getMsisdnActivationStatus());
      accountUser.setStageName(entity.getStageName());
      accountUser.setState(entity.getProvince());
      accountUser.setCountry(entity.getCountryCode());
      accountUser.setDateOfBirth(entity.getDateOfBirth());
      if (entity.getUser().getEmailActivationStatus() == null) {
        accountUser.setEmailActivationStatus(Boolean.FALSE);
      }
      if (entity.getUser().getMsisdnActivationStatus() == null) {
        accountUser.setMsisdnActivationStatus(Boolean.FALSE);
      }
      accountUser.setStatus(entity.getUser().getAuthorizationStatus());
      accountUser.setId(entity.getId());
      if (entity.getGenres() != null) {
        accountUser.setGenres(
            entity.getGenres().stream()
                .map(genreConverter::createResponse)
                .map(Optional::get)
                .collect(Collectors.toList()));
      }
      if (entity.getUser().getProfile() != null) {
        accountUser.setProfile(
            this.profileConverter.createResponse(entity.getUser().getProfile()).orElse(null));
      }
      if (entity.getFollowerCount() == null) {
        accountUser.setFollowerCount(0);
      } else {
        accountUser.setFollowerCount(entity.getFollowerCount());
      }
      if (entity.getFollowingCount() == null) {
        accountUser.setFollowingCount(0);
      } else {
        accountUser.setFollowingCount(entity.getFollowingCount());
      }
      if (entity.getProfileUrl() != null) {
        accountUser.setProfileUrl(mediaImageUrl.concat(entity.getProfileUrl()));
      }
      if (entity.getUser().getLastKnownLocation() != null) {
        accountUser.setLastKnownLocation(new Location());
        accountUser
            .getLastKnownLocation()
            .setLatitude(entity.getUser().getLastKnownLocation().getLatitude());
        accountUser
            .getLastKnownLocation()
            .setLongitude(entity.getUser().getLastKnownLocation().getLongitude());
        accountUser
            .getLastKnownLocation()
            .setCity(entity.getUser().getLastKnownLocation().getCity());
        accountUser
            .getLastKnownLocation()
            .setCountry(entity.getUser().getLastKnownLocation().getCountry());
      }
      return Optional.ofNullable(accountUser);
    }
    return Optional.empty();
  }
}
