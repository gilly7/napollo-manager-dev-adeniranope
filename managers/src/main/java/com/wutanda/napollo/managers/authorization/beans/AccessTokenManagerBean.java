package com.wutanda.napollo.managers.authorization.beans;

import com.google.common.base.Splitter;
import com.wutanda.napollo.api.v1_0.authorization.common.AccessToken;
import com.wutanda.napollo.api.v1_0.location.LocationRequest;
import com.wutanda.napollo.common.authorization.Authorization;
import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.common.transport.AbstractResponse;
import com.wutanda.napollo.managers.authorization.AccessTokenManager;
import com.wutanda.napollo.persistence.location.LocationEntity;
import com.wutanda.napollo.persistence.location.dao.LocationEntityDao;
import com.wutanda.napollo.persistence.users.UserEntity;
import com.wutanda.napollo.persistence.users.dao.UserEntityDao;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Component
public class AccessTokenManagerBean implements AccessTokenManager {

  @Autowired private UserEntityDao userEntityDao;

  @Value("${napollo.authorization.key}")
  private String jwtTokenAuthorizationKey;

  @Value("${napollo.authorization.expiry.seconds}")
  private String expirySeconds;

  @Autowired private LocationEntityDao locationEntityDao;

  @Override
  public AbstractResponse<AccessToken> createAccessToken(
      Authorization authorization, final LocationRequest locationRequest) throws NapolloException {
    final List<String> authorizationParameters =
        Splitter.on(":")
            .splitToList(
                new String(
                    Base64.getDecoder()
                        .decode(authorization.getAuthorizationToken().replace("Basic", ""))));
    final UserEntity authorizedUserEntity =
        this.userEntityDao
            .findByEmailAddressAndPassword(
                authorizationParameters.get(0), authorizationParameters.get(1))
            .orElseThrow(
                () ->
                    NapolloException.authorizationError(
                        "Cannot authorize this user. Please try again"));
    final LocalDateTime expiryLocalDateTime =
        LocalDateTime.now().plusSeconds(Integer.parseInt(expirySeconds) * 24);
    final JwtBuilder jwtBuilder = Jwts.builder();
    jwtBuilder.setIssuer("Napollo Music Manager v1.0");
    jwtBuilder.signWith(SignatureAlgorithm.HS512, jwtTokenAuthorizationKey);
    jwtBuilder.setExpiration(expiryLocalDateTime.toDate());
    jwtBuilder.claim("userIdentity", authorizedUserEntity.getId());
    final AccessToken accessToken = new AccessToken();
    accessToken.setAccessToken(jwtBuilder.compact());
    accessToken.setAccessType(authorizedUserEntity.getProfile().getProfileType());
    accessToken.setExpires(expiryLocalDateTime.toDate().getTime());
    accessToken.setPermissions(authorizedUserEntity.getProfile().getPermissions());
    if (locationRequest != null) {
      LocationEntity persistedLocationEntity = new LocationEntity();
      final Optional<LocationEntity> locationEntityOptional =
          locationEntityDao.findByUser_Id(authorizedUserEntity.getId());
      if (!locationEntityOptional.isPresent()) {
        persistedLocationEntity.setUser(authorizedUserEntity);
      } else {
        persistedLocationEntity = locationEntityOptional.get();
        persistedLocationEntity.setLatitude(locationRequest.getLatitude());
        persistedLocationEntity.setLongitude(locationRequest.getLongitude());
        persistedLocationEntity.setCity(locationRequest.getCity());
        persistedLocationEntity.setCountry(locationRequest.getCountry());
      }
      authorizedUserEntity.setLastKnownLocation(locationEntityDao.save(persistedLocationEntity));
      userEntityDao.save(authorizedUserEntity);
    }
    return AbstractResponse.response(
        accessToken, "Access token created successfully", Boolean.TRUE);
  }
}
