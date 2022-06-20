package com.wutanda.napollo.frontend.music;

import com.wutanda.napollo.api.v1_0.music.GenreRequest;
import com.wutanda.napollo.api.v1_0.music.common.Genre;
import com.wutanda.napollo.common.authorization.Authorization;
import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.common.transport.AbstractResponse;
import com.wutanda.napollo.managers.music.GenreManager;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = {"Music APIs"})
public class GenreController {

  @Autowired private GenreManager genreManager;

  @Operation(
      tags = {"Music APIs"},
      summary = "Create a music genre",
      description = "Create a new music genre")
  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "genre")
  public ResponseEntity<AbstractResponse> create(
      @RequestHeader("Authorization") String authorization, @RequestBody GenreRequest genreRequest)
      throws NapolloException {
    return ResponseEntity.ok(
        this.genreManager.create(Authorization.bearerAuthorization(authorization), genreRequest));
  }

  @Operation(
      tags = {"Music APIs"},
      summary = "Get a genre information",
      description = "Get a genre information")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "genre/{id}")
  public ResponseEntity<AbstractResponse<Genre>> get(
      @RequestHeader("Authorization") String authorization,
      @PathVariable("id") String genreIdentity)
      throws NapolloException {
    return ResponseEntity.ok(
        this.genreManager.get(Authorization.bearerAuthorization(authorization), genreIdentity));
  }

  @Operation(
      tags = {"Music APIs"},
      summary = "Get a list of genre information",
      description = "Get a list of genre information")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "genres")
  public ResponseEntity<AbstractResponse<Page<Genre>>> list(
      @RequestHeader("Authorization") String authorization,
      @RequestParam("page") Integer page,
      @RequestParam("size") Integer size)
      throws NapolloException {
    return ResponseEntity.ok(
        this.genreManager.list(Authorization.bearerAuthorization(authorization), page, size));
  }

  @Operation(
      tags = {"Music APIs"},
      summary = "Update a music genre",
      description = "Update a music genre")
  @PutMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "genre/{id}")
  public ResponseEntity<AbstractResponse> update(
      @RequestHeader("Authorization") String authorization,
      @PathVariable("id") String genreIdentity,
      @RequestBody GenreRequest genreRequest)
      throws NapolloException {
    return ResponseEntity.ok(
        this.genreManager.update(
            Authorization.bearerAuthorization(authorization), genreIdentity, genreRequest));
  }

  @Operation(
      tags = {"Music APIs"},
      summary = "Delete a genre information",
      description = "Delete a genre information")
  @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "genre/{id}")
  public ResponseEntity<AbstractResponse> delete(
      @RequestHeader("Authorization") String authorization,
      @PathVariable("id") String genreIdentity)
      throws NapolloException {
    return ResponseEntity.ok(
        this.genreManager.delete(Authorization.bearerAuthorization(authorization), genreIdentity));
  }
}
