package com.wutanda.napollo.managers.music.beans;

import com.wutanda.napollo.api.v1_0.music.CommentRequest;
import com.wutanda.napollo.api.v1_0.music.common.Comment;
import com.wutanda.napollo.common.activity.ActivityRequest;
import com.wutanda.napollo.common.activity.ActivityType;
import com.wutanda.napollo.common.authorization.Authorization;
import com.wutanda.napollo.common.authorization.PermissionRegistry;
import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.common.transport.AbstractResponse;
import com.wutanda.napollo.converters.music.CommentConverter;
import com.wutanda.napollo.managers.activity.ActivityManager;
import com.wutanda.napollo.managers.authorization.PermissionManager;
import com.wutanda.napollo.managers.music.CommentManager;
import com.wutanda.napollo.persistence.accountusers.dao.AccountUserEntityDao;
import com.wutanda.napollo.persistence.music.CommentEntity;
import com.wutanda.napollo.persistence.music.MediaEntity;
import com.wutanda.napollo.persistence.music.dao.CommentEntityDao;
import com.wutanda.napollo.persistence.music.dao.MediaEntityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CommentManagerBean implements CommentManager {

  @Autowired private CommentEntityDao commentEntityDao;

  @Autowired private PermissionManager permissionManager;

  @Autowired private AccountUserEntityDao accountUserEntityDao;

  @Autowired private CommentConverter commentConverter;

  @Autowired private MediaEntityDao mediaEntityDao;

  @Autowired private ActivityManager activityManager;

  @Override
  @Transactional
  public AbstractResponse create(Authorization authorization, CommentRequest request)
      throws NapolloException {
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.CREATE_MEDIA_COMMENT.name()));
    final MediaEntity mediaEntity =
        this.mediaEntityDao
            .findById(request.getMediaId())
            .orElseThrow(
                () -> NapolloException.internalError("Cannot comment on an unavailable media"));
    if (request.getCommentId() != null) {
      // post a reply
      final CommentEntity commentEntity =
          this.commentEntityDao
              .findById(request.getCommentId())
              .orElseThrow(() -> NapolloException.internalError("Comment not available"));
      final CommentEntity replyEntity = new CommentEntity();
      replyEntity.setComment(request.getComment());
      replyEntity.setAccountUser(
          this.accountUserEntityDao
              .findByUser_EmailAddress(
                  this.permissionManager.getAuthorizedUser(authorization).getEmailAddress())
              .orElseThrow(
                  () -> NapolloException.authorizationError("Cannot authorize this operation")));
      replyEntity.setMedia(mediaEntity);
      replyEntity.setCreatedUser(replyEntity.getAccountUser().getUser().getId());
      replyEntity.setReplyState(Boolean.TRUE);
      this.commentEntityDao.save(replyEntity);
      commentEntity.getReplies().add(replyEntity);
      this.commentEntityDao.save(commentEntity);
      final ActivityRequest activityRequest = new ActivityRequest();
      activityRequest.setActivityType(ActivityType.REPLY);
      activityRequest.setActivitySourceOwnerId(commentEntity.getId());
      activityRequest.setActivityDestinationOwnerId(replyEntity.getId());
      activityRequest.setActivityUserId(permissionManager.getAuthorizedUser(authorization).getId());
      activityManager.postActivity(authorization, activityRequest);
      return AbstractResponse.response("Reply saved successfully", Boolean.TRUE);
    } else {
      final CommentEntity commentEntity = new CommentEntity();
      commentEntity.setComment(request.getComment());
      commentEntity.setAccountUser(
          this.accountUserEntityDao
              .findByUser_EmailAddress(
                  this.permissionManager.getAuthorizedUser(authorization).getEmailAddress())
              .orElseThrow(
                  () -> NapolloException.authorizationError("Cannot authorize this operation")));
      commentEntity.setMedia(mediaEntity);
      commentEntity.setReplyState(Boolean.FALSE);
      commentEntity.setCreatedUser(commentEntity.getAccountUser().getUser().getId());
      this.commentEntityDao.save(commentEntity);
      if (mediaEntity.getMediaComments() == null) {
        mediaEntity.setMediaComments(new ArrayList<>());
      }
      mediaEntity.getMediaComments().add(commentEntity);
      this.mediaEntityDao.save(mediaEntity);
      final ActivityRequest activityRequest = new ActivityRequest();
      activityRequest.setActivityType(ActivityType.COMMENT);
      activityRequest.setActivitySourceOwnerId(commentEntity.getId());
      activityRequest.setActivityDestinationOwnerId(commentEntity.getId());
      activityRequest.setActivityUserId(permissionManager.getAuthorizedUser(authorization).getId());
      activityManager.postActivity(authorization, activityRequest);
      return AbstractResponse.response("Comments saved successfully", Boolean.TRUE);
    }
  }

  @Override
  public AbstractResponse<Comment> get(Authorization authorization, String identity)
      throws NapolloException {
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.GET_MEDIA_COMMENT.name()));
    final CommentEntity commentEntity =
        this.commentEntityDao
            .findById(identity)
            .orElseThrow(() -> NapolloException.internalError("Comment not available"));
    return AbstractResponse.response(
        this.commentConverter
            .createResponse(commentEntity)
            .orElseThrow(() -> NapolloException.internalError("Unable to complete this operation")),
        "Comment fetched successfully",
        Boolean.TRUE);
  }

  @Override
  public AbstractResponse<Page<Comment>> list(
      Authorization authorization, Integer page, Integer size) throws NapolloException {
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.GET_MEDIA_COMMENTS.name()));
    final Page<CommentEntity> commentEntityPage =
        this.commentEntityDao.findAll(PageRequest.of(page, size));
    final Page<Comment> commentPage =
        new PageImpl(
            commentEntityPage.stream()
                .map(this.commentConverter::createResponse)
                .map(Optional::get)
                .collect(Collectors.toList()),
            commentEntityPage.getPageable(),
            commentEntityPage.getTotalElements());
    return AbstractResponse.response(commentPage, "Comments fetched successfully", Boolean.TRUE);
  }

  @Override
  @Transactional
  public AbstractResponse update(
      Authorization authorization, String identity, CommentRequest request)
      throws NapolloException {
    return null;
  }

  @Override
  @Transactional
  public AbstractResponse delete(Authorization authorization, String identity)
      throws NapolloException {
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.DELETE_MEDIA_COMMENT.name()));
    final CommentEntity commentEntity =
        this.commentEntityDao
            .findById(identity)
            .orElseThrow(() -> NapolloException.internalError("Comment not available"));
    final MediaEntity mediaEntity = commentEntity.getMedia();
    mediaEntity.getMediaComments().remove(commentEntity);
    this.mediaEntityDao.save(mediaEntity);
    this.commentEntityDao.delete(commentEntity);
    return AbstractResponse.response("Comment removed successfully", Boolean.TRUE);
  }

  @Override
  public AbstractResponse<Page<Comment>> mediaComments(
      Authorization authorization, String mediaIdentity, Integer page, Integer size)
      throws NapolloException {
    this.permissionManager.validatePermission(
        authorization, Arrays.asList(PermissionRegistry.GET_MEDIA_COMMENTS.name()));
    final Page<CommentEntity> commentEntityPage =
        this.commentEntityDao
            .findByMedia_IdAndReplyStateFalse(
                mediaIdentity,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp")))
            .orElseThrow(() -> NapolloException.internalError("No comments available"));
    final Page<Comment> commentPage =
        new PageImpl(
            commentEntityPage.stream()
                .map(this.commentConverter::createResponse)
                .map(Optional::get)
                .collect(Collectors.toList()),
            commentEntityPage.getPageable(),
            commentEntityPage.getTotalElements());
    return AbstractResponse.response(commentPage, "Comments fetched successfully", Boolean.TRUE);
  }
}
