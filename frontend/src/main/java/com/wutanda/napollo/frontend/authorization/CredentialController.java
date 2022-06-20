package com.wutanda.napollo.frontend.authorization;

import com.wutanda.napollo.api.v1_0.authorization.CredentialRequest;
import com.wutanda.napollo.common.authorization.Authorization;
import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.common.transport.AbstractResponse;
import com.wutanda.napollo.managers.authorization.CredentialManager;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = {"Password APIs"})
public class CredentialController {

  @Autowired private CredentialManager credentialManager;

  @Operation(
      tags = {"Password APIs"},
      summary = "Create a new credential token for password reset")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "password/otp")
  public ResponseEntity<AbstractResponse> createCredentialOTP(
      @RequestHeader("Authorization") String authorization,
      @RequestParam("email") String emailAddress)
      throws NapolloException {
    return ResponseEntity.ok(
        this.credentialManager.createCredentialOTP(
            Authorization.bearerAuthorization(authorization), emailAddress));
  }

  @Operation(
      tags = {"Password APIs"},
      summary = "Reset the password of a user with the generated OTP")
  @PostMapping(
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      value = "password")
  public ResponseEntity<AbstractResponse> resetCredentialWithOTP(
      @RequestHeader("Authorization") String authorization,
      @RequestParam("otp") String otp,
      @RequestBody CredentialRequest credentialRequest)
      throws NapolloException {
    return ResponseEntity.ok(
        this.credentialManager.resetCredentialWithOTP(
            Authorization.bearerAuthorization(authorization), otp, credentialRequest));
  }

  @Operation(
      tags = {"Password APIs"},
      summary = "Change the password of an authorized user")
  @PutMapping(
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      value = "password")
  public ResponseEntity<AbstractResponse> changeCredential(
      @RequestHeader("Authorization") String authorization,
      @RequestBody CredentialRequest credentialRequest)
      throws NapolloException {
    return ResponseEntity.ok(
        this.credentialManager.changeCredential(
            Authorization.bearerAuthorization(authorization), credentialRequest));
  }
}
