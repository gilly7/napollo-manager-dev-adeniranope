package com.wutanda.napollo.frontend.authorization;

import com.wutanda.napollo.api.v1_0.authorization.common.AccessToken;
import com.wutanda.napollo.api.v1_0.location.LocationRequest;
import com.wutanda.napollo.common.authorization.Authorization;
import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.common.transport.AbstractResponse;
import com.wutanda.napollo.managers.authorization.AccessTokenManager;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = {"Access APIs"})
public class AccessTokenController {

  @Autowired private AccessTokenManager accessTokenManager;

  @Operation(
      tags = {"Access APIs"},
      summary = "Create access token for login")
  @PostMapping(
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "login",
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AbstractResponse<AccessToken>> createAccessToken(
      @RequestHeader("Authorization") String authorization,
      @RequestBody(required = false) LocationRequest locationRequest)
      throws NapolloException {
    return ResponseEntity.ok(
        this.accessTokenManager.createAccessToken(
            Authorization.basicAuthorization(authorization), locationRequest));
  }
}
