package com.wutanda.napollo.frontend.music;

import com.wutanda.napollo.api.v1_0.music.PlaylistRequest;
import com.wutanda.napollo.api.v1_0.music.common.Playlist;
import com.wutanda.napollo.common.authorization.Authorization;
import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.common.transport.AbstractResponse;
import com.wutanda.napollo.managers.music.PlaylistManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class PlaylistController {

  @Autowired private PlaylistManager playlistManager;

  @PostMapping(
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "playlist")
  public ResponseEntity<AbstractResponse> create(
      @RequestHeader("Authorization") String authorization,
      @RequestParam(value = "artFile", required = false) MultipartFile multipartFile,
      @RequestParam("name") String name,
      @RequestParam("description") String description,
      @RequestParam("visible") Boolean visible)
      throws NapolloException {
    final PlaylistRequest playlistRequest = new PlaylistRequest();
    playlistRequest.setDescription(description);
    playlistRequest.setName(name);
    playlistRequest.setVisible(visible);
    playlistRequest.setArtMultipartFile(multipartFile);
    return ResponseEntity.ok(
        this.playlistManager.create(
            Authorization.bearerAuthorization(authorization), playlistRequest));
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "playlist/{id}")
  public ResponseEntity<AbstractResponse<Playlist>> get(
      @RequestHeader("Authorization") String authorization,
      @PathVariable("id") String playlistIdentity)
      throws NapolloException {
    return ResponseEntity.ok(
        this.playlistManager.get(
            Authorization.bearerAuthorization(authorization), playlistIdentity));
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "playlist/{id}/media")
  public ResponseEntity<AbstractResponse> get(
      @RequestHeader("Authorization") String authorization,
      @PathVariable("id") String playlistIdentity,
      @RequestParam("id") String mediaIdentity,
      @RequestParam("state") Boolean state)
      throws NapolloException {
    return ResponseEntity.ok(
        this.playlistManager.addMedia(
            Authorization.bearerAuthorization(authorization),
            playlistIdentity,
            mediaIdentity,
            state));
  }

  @PutMapping(
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "playlist/{id}")
  public ResponseEntity<AbstractResponse> update(
      @RequestHeader("Authorization") String authorization,
      @PathVariable("id") String playlistIdentity,
      @RequestParam(value = "artFile", required = false) MultipartFile multipartFile,
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "description", required = false) String description,
      @RequestParam(value = "visible", required = false) Boolean visible)
      throws NapolloException {
    final PlaylistRequest playlistRequest = new PlaylistRequest();
    playlistRequest.setDescription(description);
    playlistRequest.setName(name);
    playlistRequest.setVisible(visible);
    playlistRequest.setArtMultipartFile(multipartFile);
    return ResponseEntity.ok(
        this.playlistManager.update(
            Authorization.bearerAuthorization(authorization), playlistIdentity, playlistRequest));
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "playlists")
  public ResponseEntity<AbstractResponse<Page<Playlist>>> get(
      @RequestHeader("Authorization") String authorization,
      @RequestParam(value = "publicState", required = false) Boolean status,
      @RequestParam("page") Integer page,
      @RequestParam("size") Integer size)
      throws NapolloException {
    return ResponseEntity.ok(
        this.playlistManager.listByStatus(
            Authorization.bearerAuthorization(authorization), status, page, size));
  }

  @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "playlist/{id}")
  public ResponseEntity<AbstractResponse> delete(
      @RequestHeader("Authorization") String authorization,
      @PathVariable("id") String playlistIdentity)
      throws NapolloException {
    return ResponseEntity.ok(
        this.playlistManager.delete(
            Authorization.bearerAuthorization(authorization), playlistIdentity));
  }
}
