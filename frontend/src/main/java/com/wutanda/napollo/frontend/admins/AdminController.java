package com.wutanda.napollo.frontend.admins;

import com.wutanda.napollo.api.v1_0.admin.AdminRequest;
import com.wutanda.napollo.api.v1_0.admin.common.Admin;
import com.wutanda.napollo.common.authorization.Authorization;
import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.common.transport.AbstractResponse;
import com.wutanda.napollo.managers.admin.AdminManager;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Api("Admin APIs")
public class AdminController {

  @Autowired private AdminManager adminManager;

  @Operation(
      tags = {"Admin APIs"},
      description = "Create a new administrator account",
      summary = "Setup a new admin account for backend access")
  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "admin")
  public ResponseEntity<AbstractResponse> create(
      @RequestHeader("Authorization") String authorization, @RequestBody AdminRequest adminRequest)
      throws NapolloException {
    return ResponseEntity.ok(
        this.adminManager.create(Authorization.bearerAuthorization(authorization), adminRequest));
  }

  @Operation(
      tags = {"Admin APIs"},
      description = "Get an administrator account",
      summary = "Get an administrator account")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "admin/{id}")
  public ResponseEntity<AbstractResponse<Admin>> get(
      @RequestHeader("Authorization") String authorization,
      @PathVariable("id") String adminIdentity)
      throws NapolloException {
    return ResponseEntity.ok(
        this.adminManager.get(Authorization.bearerAuthorization(authorization), adminIdentity));
  }

  @Operation(
      tags = {"Admin APIs"},
      description = "List all administrator accounts",
      summary = "List all administrator account")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "admins")
  public ResponseEntity<AbstractResponse<Page<Admin>>> list(
      @RequestHeader("Authorization") String authorization,
      @RequestParam("page") Integer page,
      @RequestParam("size") Integer size)
      throws NapolloException {
    return ResponseEntity.ok(
        this.adminManager.list(Authorization.bearerAuthorization(authorization), page, size));
  }

  @Operation(
      tags = {"Admin APIs"},
      description = "Update administrator accounts",
      summary = "Update administrator account")
  @PutMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "admin/{id}")
  public ResponseEntity<AbstractResponse> update(
      @RequestHeader("Authorization") String authorization,
      @PathVariable("id") String adminIdentity,
      @RequestBody AdminRequest adminRequest)
      throws NapolloException {
    return ResponseEntity.ok(
        this.adminManager.update(
            Authorization.bearerAuthorization(authorization), adminIdentity, adminRequest));
  }

  @Operation(
      tags = {"Admin APIs"},
      description = "Delete an administrator account",
      summary = "Delete an administrator account")
  @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "admin/{id}")
  public ResponseEntity<AbstractResponse> close(
      @RequestHeader("Authorization") String authorization,
      @PathVariable("id") String adminIdentity)
      throws NapolloException {
    return ResponseEntity.ok(
        this.adminManager.delete(Authorization.bearerAuthorization(authorization), adminIdentity));
  }

  @Operation(
      tags = {"Admin APIs"},
      summary = "Block an administrator account")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "admin/{id}/block")
  public ResponseEntity<AbstractResponse> block(
      @RequestHeader("Authorization") String authorization,
      @PathVariable("id") String adminIdentity)
      throws NapolloException {
    return ResponseEntity.ok(
        this.adminManager.block(Authorization.bearerAuthorization(authorization), adminIdentity));
  }

  @Operation(
      tags = {"Admin APIs"},
      summary = "Unblock an administrator account")
  @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "admin/{id}/block")
  public ResponseEntity<AbstractResponse> unblock(
      @RequestHeader("Authorization") String authorization,
      @PathVariable("id") String adminIdentity)
      throws NapolloException {
    return ResponseEntity.ok(
        this.adminManager.unblock(Authorization.bearerAuthorization(authorization), adminIdentity));
  }
}
