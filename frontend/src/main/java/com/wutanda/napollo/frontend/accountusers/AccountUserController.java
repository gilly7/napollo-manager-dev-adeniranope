package com.wutanda.napollo.frontend.accountusers;

import com.wutanda.napollo.api.v1_0.accountusers.AccountUserRequest;
import com.wutanda.napollo.api.v1_0.accountusers.EmailActivationRequest;
import com.wutanda.napollo.api.v1_0.accountusers.MsisdnActivationRequest;
import com.wutanda.napollo.api.v1_0.accountusers.common.AccountUser;
import com.wutanda.napollo.common.authorization.Authorization;
import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.common.transport.AbstractResponse;
import com.wutanda.napollo.managers.accountusers.AccountUserManager;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Api("Account User APIs")
public class AccountUserController {

  @Autowired private AccountUserManager accountUserManager;

  @Operation(
      tags = {"Account User APIs"},
      summary = "Register a new account user")
  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "accountuser")
  public ResponseEntity<AbstractResponse> createAccountUser(
      @RequestHeader("Authorization") String authorization,
      @RequestBody AccountUserRequest accountUserRequest)
      throws NapolloException {
    return ResponseEntity.ok(
        this.accountUserManager.create(
            Authorization.bearerAuthorization(authorization), accountUserRequest));
  }

  @Operation(
      tags = {"Account User APIs"},
      summary = "Get an account user information with the ID")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "accountuser/{id}")
  public ResponseEntity<AbstractResponse<AccountUser>> getAccountUser(
      @RequestHeader("Authorization") String authorization,
      @PathVariable("id") String accountUserIdentity)
      throws NapolloException {
    return ResponseEntity.ok(
        this.accountUserManager.get(
            Authorization.bearerAuthorization(authorization), accountUserIdentity));
  }

  @Operation(
      tags = {"Account User APIs"},
      summary = "Get an account user information of an authorized user")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "accountuser")
  public ResponseEntity<AbstractResponse<AccountUser>> getAccountUser(
      @RequestHeader("Authorization") String authorization) throws NapolloException {
    return ResponseEntity.ok(
        this.accountUserManager.getAccountUser(Authorization.bearerAuthorization(authorization)));
  }

  @Operation(
      tags = {"Account User APIs"},
      summary = "List all registered account users")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "accountusers")
  public ResponseEntity<AbstractResponse<Page<AccountUser>>> listAccountUsers(
      @RequestHeader("Authorization") String authorization,
      @RequestParam("page") Integer page,
      @RequestParam("size") Integer size)
      throws NapolloException {
    return ResponseEntity.ok(
        this.accountUserManager.list(Authorization.bearerAuthorization(authorization), page, size));
  }

  @Operation(
      tags = {"Account User APIs"},
      summary = "Update the account user information with the account user ID")
  @PutMapping(
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      value = "accountuser/{id}")
  public ResponseEntity<AbstractResponse> updateAccountUser(
      @RequestHeader("Authorization") String authorization,
      @PathVariable("id") String accountUserIdentity,
      @RequestBody AccountUserRequest accountUserRequest)
      throws NapolloException {
    return ResponseEntity.ok(
        this.accountUserManager.update(
            Authorization.bearerAuthorization(authorization),
            accountUserIdentity,
            accountUserRequest));
  }

  @Operation(
      tags = {"Account User APIs"},
      summary = "Update the account user information of an authorized user")
  @PutMapping(
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      value = "accountuser")
  public ResponseEntity<AbstractResponse> updateAccountUser(
      @RequestHeader("Authorization") String authorization,
      @RequestBody AccountUserRequest accountUserRequest)
      throws NapolloException {
    return ResponseEntity.ok(
        this.accountUserManager.updateAccountUser(
            Authorization.bearerAuthorization(authorization), accountUserRequest));
  }

  @Operation(
      tags = {"Account User APIs"},
      summary = "Delete the account user information with the account user ID.")
  @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "accountuser/{id}")
  public ResponseEntity<AbstractResponse> deleteAccountUser(
      @RequestHeader("Authorization") String authorization,
      @PathVariable("id") String accountUserIdentity)
      throws NapolloException {
    return ResponseEntity.ok(
        this.accountUserManager.delete(
            Authorization.bearerAuthorization(authorization), accountUserIdentity));
  }

  @Operation(
      tags = {"Account User APIs"},
      summary = "Upgrade the account user with the account user ID")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "accountuser/{id}/upgrade")
  public ResponseEntity<AbstractResponse> upgradeAccountUser(
      @RequestHeader("Authorization") String authorization,
      @PathVariable("id") String accountUserIdentity,
      @RequestParam("userType") String userType)
      throws NapolloException {
    return ResponseEntity.ok(
        this.accountUserManager.upgradeAccountUser(
            Authorization.bearerAuthorization(authorization), accountUserIdentity, userType));
  }

  @Operation(
      tags = {"Account User APIs"},
      summary = "Approve the upgrade of an account user with the account user ID")
  @GetMapping(
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "accountuser/{id}/upgrade/approve")
  public ResponseEntity<AbstractResponse> approveUpgradeAccountUser(
      @RequestHeader("Authorization") String authorization,
      @PathVariable("id") String accountUserIdentity,
      @RequestParam("profileId") String profileIdentity,
      @RequestParam("status") Boolean approvalStatus)
      throws NapolloException {
    return ResponseEntity.ok(
        this.accountUserManager.approveUpgradeAccountUser(
            Authorization.bearerAuthorization(authorization),
            accountUserIdentity,
            profileIdentity,
            approvalStatus));
  }

  @Operation(
      tags = {"Account User APIs"},
      summary =
          "Verify and activate an account with the activation code sent to the email address of an account user")
  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "accountuser/activation/email")
  public ResponseEntity<AbstractResponse> emailActivation(
      @RequestHeader("Authorization") String authorization,
      @RequestBody EmailActivationRequest emailActivationRequest)
      throws NapolloException {
    return ResponseEntity.ok(
        this.accountUserManager.emailActivation(
            Authorization.bearerAuthorization(authorization), emailActivationRequest));
  }

  @Operation(
      tags = {"Account User APIs"},
      summary = "Resend the activation code sent to the email address of an account user")
  @PutMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "accountuser/activation/email")
  public ResponseEntity<AbstractResponse> resendEmailActivation(
      @RequestHeader("Authorization") String authorization,
      @RequestBody EmailActivationRequest emailActivationRequest)
      throws NapolloException {
    return ResponseEntity.ok(
        this.accountUserManager.resendEmailActivationCode(
            Authorization.bearerAuthorization(authorization), emailActivationRequest));
  }

  @Operation(
      tags = {"Account User APIs"},
      summary =
          "Verify and activate an account with the activation code sent to the mobile number of an account user")
  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "accountuser/activation/msisdn")
  public ResponseEntity<AbstractResponse> msisdnActivation(
      @RequestHeader("Authorization") String authorization,
      @RequestBody MsisdnActivationRequest msisdnActivationRequest)
      throws NapolloException {
    return ResponseEntity.ok(
        this.accountUserManager.msisdnActivation(
            Authorization.bearerAuthorization(authorization), msisdnActivationRequest));
  }

  @Operation(
      tags = {"Account User APIs"},
      summary = "Resend the activation code sent to the mobile number of an account user")
  @PutMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "accountuser/activation/msisdn")
  public ResponseEntity<AbstractResponse> resendMsisdnActivation(
      @RequestHeader("Authorization") String authorization,
      @RequestBody MsisdnActivationRequest msisdnActivationRequest)
      throws NapolloException {
    return ResponseEntity.ok(
        this.accountUserManager.resendMsisdnActivationCode(
            Authorization.bearerAuthorization(authorization), msisdnActivationRequest));
  }

  @Operation(
      tags = {"Account User APIs"},
      summary = "Assign a genre to an account user")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "accountuser/{id}/genre")
  public ResponseEntity<AbstractResponse> assignAccountUserGenre(
      @RequestHeader("Authorization") String authorization,
      @PathVariable("id") String accountUserIdentity,
      @RequestParam("genreId") String genreIdentity)
      throws NapolloException {
    return ResponseEntity.ok(
        this.accountUserManager.assignAccountUserGenre(
            Authorization.bearerAuthorization(authorization),
            accountUserIdentity,
            genreIdentity,
            Boolean.TRUE));
  }

  @Operation(
      tags = {"Account User APIs"},
      summary = "Unassign a genre to an account user")
  @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "accountuser/{id}/genre")
  public ResponseEntity<AbstractResponse> unassignAccountUserGenre(
      @RequestHeader("Authorization") String authorization,
      @PathVariable("id") String accountUserIdentity,
      @RequestParam("genreId") String genreIdentity)
      throws NapolloException {
    return ResponseEntity.ok(
        this.accountUserManager.assignAccountUserGenre(
            Authorization.bearerAuthorization(authorization),
            accountUserIdentity,
            genreIdentity,
            Boolean.FALSE));
  }

  @Operation(
      tags = {"Account User APIs"},
      summary = "Update the account user profile picture")
  @PutMapping(
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "accountuser/photo")
  public ResponseEntity<AbstractResponse> updateProfilePhoto(
      @RequestHeader("Authorization") String authorization,
      @RequestParam("photo") MultipartFile multipartFile)
      throws NapolloException {
    return ResponseEntity.ok(
        this.accountUserManager.updateProfilePicture(
            Authorization.bearerAuthorization(authorization), multipartFile));
  }

  @Operation(
      tags = {"Account User APIs"},
      summary = "Follow/unfollow an account user")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "accountuser/follow")
  public ResponseEntity<AbstractResponse> followAccountUser(
      @RequestHeader("Authorization") String authorization,
      @RequestParam("id") String accountUserIdentity,
      @RequestParam("state") Boolean followState)
      throws NapolloException {
    return ResponseEntity.ok(
        this.accountUserManager.followAccountUser(
            Authorization.bearerAuthorization(authorization), accountUserIdentity, followState));
  }
}
