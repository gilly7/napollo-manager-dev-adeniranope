package com.wutanda.napollo.frontend.music;

import com.wutanda.napollo.api.v1_0.music.AlbumRequest;
import com.wutanda.napollo.api.v1_0.music.common.Album;
import com.wutanda.napollo.common.authorization.Authorization;
import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.common.transport.AbstractResponse;
import com.wutanda.napollo.managers.music.AlbumManager;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Api(
    value = "Music Album APIs",
    tags = {"Music APIs"})
public class AlbumController {

  @Autowired private AlbumManager albumManager;

  @Operation(
      tags = {"Music APIs"},
      summary = "Create a new music album",
      description = "Only artists are allowed to create a music album")
  @PostMapping(
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "album")
  public ResponseEntity<AbstractResponse> create(
      @RequestHeader("Authorization") String authorization,
      @RequestParam("albumArt") MultipartFile multipartFile,
      @RequestParam("name") String name,
      @RequestParam("description") String description,
      @RequestParam("year") Integer year)
      throws NapolloException {
    final AlbumRequest albumRequest = new AlbumRequest();
    albumRequest.setName(name);
    albumRequest.setDescription(description);
    albumRequest.setYear(year);
    return ResponseEntity.ok(
        this.albumManager.create(
            Authorization.bearerAuthorization(authorization), multipartFile, albumRequest));
  }

  @Operation(
      tags = {"Music APIs"},
      summary = "Update a music album",
      description = "Update a defined music album")
  @PutMapping(
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "album/{id}")
  public ResponseEntity<AbstractResponse> update(
      @RequestHeader("Authorization") String authorization,
      @PathVariable("id") String albumIdentity,
      @RequestParam(value = "albumArt", required = false) MultipartFile multipartFile,
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "description", required = false) String description,
      @RequestParam(value = "year", required = false) Integer year)
      throws NapolloException {
    final AlbumRequest albumRequest = new AlbumRequest();
    albumRequest.setName(name);
    albumRequest.setDescription(description);
    albumRequest.setYear(year);
    return ResponseEntity.ok(
        this.albumManager.update(
            Authorization.bearerAuthorization(authorization),
            albumIdentity,
            multipartFile,
            albumRequest));
  }

  @Operation(
      tags = {"Music APIs"},
      summary = "Get a music album",
      description = "Get a music album information")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "album/{id}")
  public ResponseEntity<AbstractResponse<Album>> get(
      @RequestHeader("Authorization") String authorization,
      @PathVariable("id") String albumIdentity)
      throws NapolloException {
    return ResponseEntity.ok(
        this.albumManager.get(Authorization.bearerAuthorization(authorization), albumIdentity));
  }

  @Operation(
      tags = {"Music APIs"},
      summary = "Get a music album",
      description = "Add media to an album")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "album/{id}/media")
  public ResponseEntity<AbstractResponse> addMedia(
      @RequestHeader("Authorization") String authorization,
      @PathVariable("id") String albumIdentity,
      @RequestParam("media") String mediaIdentity)
      throws NapolloException {
    return ResponseEntity.ok(
        this.albumManager.addMedia(
            Authorization.bearerAuthorization(authorization), albumIdentity, mediaIdentity));
  }

  @Operation(
      tags = {"Music APIs"},
      summary = "Get a music albums",
      description = "Get all albums")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "albums")
  public ResponseEntity<AbstractResponse<Page<Album>>> list(
      @RequestHeader("Authorization") String authorization,
      @RequestParam("page") Integer page,
      @RequestParam("size") Integer size)
      throws NapolloException {
    return ResponseEntity.ok(
        this.albumManager.list(Authorization.bearerAuthorization(authorization), page, size));
  }

  @Operation(
      tags = {"Music APIs"},
      summary = "Remove a music album",
      description = "Remove a music album information")
  @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "album/{id}")
  public ResponseEntity<AbstractResponse> delete(
      @RequestHeader("Authorization") String authorization,
      @PathVariable("id") String albumIdentity)
      throws NapolloException {
    return ResponseEntity.ok(
        this.albumManager.delete(Authorization.bearerAuthorization(authorization), albumIdentity));
  }
}
