package com.wutanda.napollo.frontend.authorization;

import com.wutanda.napollo.api.v1_0.authorization.PermissionRequest;
import com.wutanda.napollo.api.v1_0.authorization.ProfileRequest;
import com.wutanda.napollo.api.v1_0.authorization.common.Profile;
import com.wutanda.napollo.common.authorization.Authorization;
import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.common.transport.AbstractResponse;
import com.wutanda.napollo.managers.authorization.PermissionManager;
import com.wutanda.napollo.managers.authorization.ProfileManager;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = {"Profile APIs"})
public class ProfileController {

  @Autowired private ProfileManager profileManager;

  @Autowired private PermissionManager permissionManager;

  @Operation(
      tags = {"Profile APIs"},
      summary = "Create a new access profiles with empty permissions")
  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "profile")
  public ResponseEntity<AbstractResponse> create(
      @RequestHeader("Authorization") String authorization,
      @RequestBody ProfileRequest profileRequest)
      throws NapolloException {
    return ResponseEntity.ok(
        this.profileManager.create(
            Authorization.bearerAuthorization(authorization), profileRequest));
  }

  @Operation(
      tags = {"Profile APIs"},
      summary = "Assign permission(s) to the access profiles")
  @PostMapping(
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      value = "profile/{id}/permission")
  public ResponseEntity<AbstractResponse> addPermission(
      @RequestHeader("Authorization") String authorization,
      @PathVariable("id") final String profileIdentity,
      @RequestBody PermissionRequest permissionRequest)
      throws NapolloException {
    return ResponseEntity.ok(
        this.permissionManager.addProfilePermission(
            Authorization.bearerAuthorization(authorization), profileIdentity, permissionRequest));
  }

  @Operation(
      tags = {"Profile APIs"},
      summary = "Unassign permission(s) to the access profiles")
  @DeleteMapping(
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      value = "profile/{id}/permission")
  public ResponseEntity<AbstractResponse> removePermission(
      @RequestHeader("Authorization") String authorization,
      @PathVariable("id") final String profileIdentity,
      @RequestBody PermissionRequest permissionRequest)
      throws NapolloException {
    return ResponseEntity.ok(
        this.permissionManager.removeProfilePermission(
            Authorization.bearerAuthorization(authorization), profileIdentity, permissionRequest));
  }

  @Operation(
      tags = {"Profile APIs"},
      summary = "Get access profile information with the profile ID")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "profile/{id}")
  public ResponseEntity<AbstractResponse<Profile>> getProfile(
      @RequestHeader("Authorization") String authorization,
      @PathVariable("id") final String profileIdentity)
      throws NapolloException {
    return ResponseEntity.ok(
        this.profileManager.get(Authorization.bearerAuthorization(authorization), profileIdentity));
  }

  @Operation(
      tags = {"Profile APIs"},
      summary = "Get the list of access profiles")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "profiles")
  public ResponseEntity<AbstractResponse<Page<Profile>>> listProfiles(
      @RequestHeader("Authorization") String authorization,
      @RequestParam("page") Integer page,
      @RequestParam("size") Integer size)
      throws NapolloException {
    return ResponseEntity.ok(
        this.profileManager.list(Authorization.bearerAuthorization(authorization), page, size));
  }

  @Operation(
      tags = {"Profile APIs"},
      summary = "Update access profile information with the profile ID")
  @PutMapping(
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      value = "profile/{id}")
  public ResponseEntity<AbstractResponse> updateProfile(
      @RequestHeader("Authorization") String authorization,
      @PathVariable("id") final String profileIdentity,
      @RequestBody ProfileRequest profileRequest)
      throws NapolloException {
    return ResponseEntity.ok(
        this.profileManager.update(
            Authorization.bearerAuthorization(authorization), profileIdentity, profileRequest));
  }

  @Operation(
      tags = {"Profile APIs"},
      summary = "Delete access profile information with the profile ID")
  @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "profile/{id}")
  public ResponseEntity<AbstractResponse> deleteProfile(
      @RequestHeader("Authorization") String authorization,
      @PathVariable("id") final String profileIdentity)
      throws NapolloException {
    return ResponseEntity.ok(
        this.profileManager.delete(
            Authorization.bearerAuthorization(authorization), profileIdentity));
  }

  @Operation(
      tags = {"Profile APIs"},
      summary = "Block access profile information with the profile ID")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "profile/{id}/block")
  public ResponseEntity<AbstractResponse> block(
      @RequestHeader("Authorization") String authorization,
      @PathVariable("id") final String profileIdentity)
      throws NapolloException {
    return ResponseEntity.ok(
        this.profileManager.blockProfile(
            Authorization.bearerAuthorization(authorization), profileIdentity));
  }

  @Operation(
      tags = {"Profile APIs"},
      summary = "Unblock access profile information with the profile ID")
  @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "profile/{id}/block")
  public ResponseEntity<AbstractResponse> unblock(
      @RequestHeader("Authorization") String authorization,
      @PathVariable("id") final String profileIdentity)
      throws NapolloException {
    return ResponseEntity.ok(
        this.profileManager.unblockProfile(
            Authorization.bearerAuthorization(authorization), profileIdentity));
  }
}
