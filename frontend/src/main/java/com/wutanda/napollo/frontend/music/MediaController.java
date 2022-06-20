package com.wutanda.napollo.frontend.music;

import com.wutanda.napollo.api.v1_0.accountusers.common.AccountUser;
import com.wutanda.napollo.api.v1_0.music.MediaRequest;
import com.wutanda.napollo.api.v1_0.music.MediaSearchRequest;
import com.wutanda.napollo.api.v1_0.music.PlayRequest;
import com.wutanda.napollo.api.v1_0.music.common.Media;
import com.wutanda.napollo.api.v1_0.music.common.Play;
import com.wutanda.napollo.common.authorization.Authorization;
import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.common.transport.AbstractResponse;
import com.wutanda.napollo.managers.music.MediaManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class MediaController {

  @Autowired private MediaManager mediaManager;

  @PostMapping(
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "media")
  public ResponseEntity<AbstractResponse<Media>> create(
      @RequestHeader("Authorization") String authorization,
      @RequestParam("mediaFile") MultipartFile mediaMultipartFile,
      @RequestParam("trailerFile") MultipartFile trailerMultipartFile,
      @RequestParam(value = "photoFile", required = false) MultipartFile photoMultipartFile,
      @RequestParam("title") String title,
      @RequestParam("description") String description,
      @RequestParam("genreId") String genreId,
      @RequestParam(value = "albumId", required = false) String albumId,
      @RequestParam(value = "featuredArtists", required = false) List<String> featuredArtists,
      @RequestParam(value = "taggedAccountUsers", required = false) List<String> taggedAccountUsers)
      throws NapolloException {
    final MediaRequest mediaRequest = new MediaRequest();
    mediaRequest.setMediaMultipartFile(mediaMultipartFile);
    mediaRequest.setPhotoMultipartFile(photoMultipartFile);
    mediaRequest.setTrailerMultipartFile(trailerMultipartFile);
    mediaRequest.setTitle(title);
    mediaRequest.setDescription(description);
    mediaRequest.setAlbumId(albumId);
    mediaRequest.setGenreId(genreId);
    mediaRequest.setFeaturedArtists(featuredArtists);
    mediaRequest.setTaggedAccountUsers(taggedAccountUsers);
    return ResponseEntity.ok(
        this.mediaManager.create(Authorization.bearerAuthorization(authorization), mediaRequest));
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "media/{id}")
  public ResponseEntity<AbstractResponse<Media>> get(
      @RequestHeader("Authorization") String authorization,
      @PathVariable("id") String mediaIdentity)
      throws NapolloException {
    return ResponseEntity.ok(
        this.mediaManager.get(Authorization.bearerAuthorization(authorization), mediaIdentity));
  }

  @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "media/{id}")
  public ResponseEntity<AbstractResponse> delete(
      @RequestHeader("Authorization") String authorization,
      @PathVariable("id") String mediaIdentity)
      throws NapolloException {
    return ResponseEntity.ok(
        this.mediaManager.delete(Authorization.bearerAuthorization(authorization), mediaIdentity));
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "medias")
  public ResponseEntity<AbstractResponse<Page<Media>>> list(
      @RequestHeader("Authorization") String authorization,
      @RequestParam("page") Integer page,
      @RequestParam("size") Integer size)
      throws NapolloException {
    return ResponseEntity.ok(
        this.mediaManager.list(Authorization.bearerAuthorization(authorization), page, size));
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "media/{id}/like")
  public ResponseEntity<AbstractResponse> like(
      @RequestHeader("Authorization") String authorization,
      @PathVariable("id") String mediaIdentity,
      @RequestParam("state") Boolean state)
      throws NapolloException {
    return ResponseEntity.ok(
        this.mediaManager.like(
            Authorization.bearerAuthorization(authorization), mediaIdentity, state));
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "media/{id}/play")
  public ResponseEntity<AbstractResponse> play(
      @RequestHeader("Authorization") String authorization,
      @PathVariable("id") String mediaIdentity,
      @RequestBody PlayRequest playRequest)
      throws NapolloException {
    return ResponseEntity.ok(
        this.mediaManager.play(
            Authorization.bearerAuthorization(authorization), mediaIdentity, playRequest));
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "media/{id}/accountuser/likes")
  public ResponseEntity<AbstractResponse<Page<AccountUser>>> likes(
      @RequestHeader("Authorization") String authorization,
      @PathVariable("id") String mediaIdentity)
      throws NapolloException {
    return ResponseEntity.ok(
        this.mediaManager.accountUserMediaLikes(
            Authorization.bearerAuthorization(authorization), mediaIdentity));
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "media/accountuser")
  public ResponseEntity<AbstractResponse<Page<Media>>> mediaAccountUser(
      @RequestHeader("Authorization") String authorization,
      @RequestParam(value = "id", required = false) String accountUserIdentity,
      @RequestParam("page") Integer page,
      @RequestParam("size") Integer size)
      throws NapolloException {
    if (accountUserIdentity != null) {
      return ResponseEntity.ok(
          this.mediaManager.accountUserMedia(
              Authorization.bearerAuthorization(authorization), accountUserIdentity, page, size));
    } else {
      return ResponseEntity.ok(
          this.mediaManager.accountUserMedia(
              Authorization.bearerAuthorization(authorization), page, size));
    }
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "media/accountuser/history")
  public ResponseEntity<AbstractResponse<Page<Play>>> mediaAccountUserHistory(
      @RequestHeader("Authorization") String authorization,
      @RequestParam("page") Integer page,
      @RequestParam("size") Integer size)
      throws NapolloException {
    return ResponseEntity.ok(
        this.mediaManager.accountUserPlayHistory(
            Authorization.bearerAuthorization(authorization), page, size));
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "media/trending")
  public ResponseEntity<AbstractResponse<Page<Media>>> mediaTrending(
      @RequestHeader("Authorization") String authorization,
      @RequestParam(value = "city", required = false) String city,
      @RequestParam(value = "state", required = false) String state,
      @RequestParam(value = "country", required = false) String country,
      @RequestParam("page") Integer page,
      @RequestParam("size") Integer size)
      throws NapolloException {
    return ResponseEntity.ok(
        this.mediaManager.trendingMedia(
            Authorization.bearerAuthorization(authorization), city, state, country, page, size));
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "media/trending/accountuser")
  public ResponseEntity<AbstractResponse<Page<AccountUser>>> accountUserTrending(
      @RequestHeader("Authorization") String authorization,
      @RequestParam(value = "city") String city,
      @RequestParam(value = "state") String state,
      @RequestParam(value = "country") String country,
      @RequestParam("page") Integer page,
      @RequestParam("size") Integer size)
      throws NapolloException {
    return ResponseEntity.ok(
        this.mediaManager.trendingAccountUser(
            Authorization.bearerAuthorization(authorization), city, state, country, page, size));
  }

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "media/discover")
  public ResponseEntity<AbstractResponse> discover(
      @RequestHeader("Authorization") String authorization,
      @RequestParam(value = "id") String mediaIdentity)
      throws NapolloException {
    return ResponseEntity.ok(
        this.mediaManager.markAsDiscovered(
            Authorization.bearerAuthorization(authorization), mediaIdentity));
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "media/discover")
  public ResponseEntity<AbstractResponse<Page<Media>>> discoverMedia(
      @RequestHeader("Authorization") String authorization,
      @RequestParam("page") Integer page,
      @RequestParam("size") Integer size)
      throws NapolloException {
    return ResponseEntity.ok(
        this.mediaManager.discoveredMedia(
            Authorization.bearerAuthorization(authorization), page, size));
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "media/discover/{id}/like")
  public ResponseEntity<AbstractResponse> likeDiscoveredMedia(
      @RequestHeader("Authorization") String authorization,
      @PathVariable(value = "id") String mediaIdentity,
      @RequestParam("status") Boolean status)
      throws NapolloException {
    return ResponseEntity.ok(
        this.mediaManager.likeDiscoveredMedia(
            Authorization.bearerAuthorization(authorization), mediaIdentity, status));
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "media/discover/likes")
  public ResponseEntity<AbstractResponse<List<Media>>> likedDiscoveredMedia(
      @RequestHeader("Authorization") String authorization) throws NapolloException {
    return ResponseEntity.ok(
        this.mediaManager.likedDiscoveredMedia(Authorization.bearerAuthorization(authorization)));
  }

  @PostMapping(
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      value = "media/search")
  public ResponseEntity<AbstractResponse<Page<Media>>> mediaSearch(
      @RequestHeader("Authorization") String authorization,
      @RequestBody MediaSearchRequest mediaSearchRequest)
      throws NapolloException {
    return ResponseEntity.ok(
        this.mediaManager.search(
            Authorization.bearerAuthorization(authorization), mediaSearchRequest));
  }
}
