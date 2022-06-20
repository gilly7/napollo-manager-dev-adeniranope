package com.wutanda.napollo.frontend.music;

import com.wutanda.napollo.api.v1_0.music.CommentRequest;
import com.wutanda.napollo.api.v1_0.music.common.Comment;
import com.wutanda.napollo.common.authorization.Authorization;
import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.common.transport.AbstractResponse;
import com.wutanda.napollo.managers.music.CommentManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommentController {

  @Autowired private CommentManager commentManager;

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE,
      value = "media/{id}/comment")
  public ResponseEntity<AbstractResponse> create(
      @RequestHeader("Authorization") String authorization,
      @PathVariable("id") String mediaIdentity,
      @RequestBody CommentRequest commentRequest)
      throws NapolloException {
    commentRequest.setMediaId(mediaIdentity);
    return ResponseEntity.ok(
        this.commentManager.create(
            Authorization.bearerAuthorization(authorization), commentRequest));
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "media/{id}/comments")
  public ResponseEntity<AbstractResponse<Page<Comment>>> list(
      @RequestHeader("Authorization") String authorization,
      @PathVariable("id") String mediaIdentity,
      @RequestParam("page") Integer page,
      @RequestParam("size") Integer size)
      throws NapolloException {
    return ResponseEntity.ok(
        this.commentManager.mediaComments(
            Authorization.bearerAuthorization(authorization), mediaIdentity, page, size));
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "media/comment/{id}")
  public ResponseEntity<AbstractResponse<Comment>> get(
      @RequestHeader("Authorization") String authorization,
      @PathVariable("id") String commentIdentity)
      throws NapolloException {
    return ResponseEntity.ok(
        this.commentManager.get(Authorization.bearerAuthorization(authorization), commentIdentity));
  }

  @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "media/comment/{id}")
  public ResponseEntity<AbstractResponse> delete(
      @RequestHeader("Authorization") String authorization,
      @PathVariable("id") String commentIdentity)
      throws NapolloException {
    return ResponseEntity.ok(
        this.commentManager.delete(
            Authorization.bearerAuthorization(authorization), commentIdentity));
  }
}
