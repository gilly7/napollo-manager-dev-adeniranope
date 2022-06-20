package com.wutanda.napollo.converters.music.beans;

import com.wutanda.napollo.api.v1_0.music.CommentRequest;
import com.wutanda.napollo.api.v1_0.music.common.Comment;
import com.wutanda.napollo.converters.accountusers.AccountUserConverter;
import com.wutanda.napollo.converters.music.CommentConverter;
import com.wutanda.napollo.converters.music.MediaConverter;
import com.wutanda.napollo.persistence.music.CommentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CommentConverterBean implements CommentConverter {

  @Autowired private AccountUserConverter accountUserConverter;

  @Autowired private MediaConverter mediaConverter;

  @Override
  public Optional<CommentEntity> createEntity(CommentRequest request) {
    return Optional.empty();
  }

  @Override
  public Optional<Comment> createResponse(CommentEntity entity) {
    if (entity != null) {
      final Comment comment = new Comment();
      comment.setComment(entity.getComment());
      comment.setAccountUser(
          this.accountUserConverter.createResponse(entity.getAccountUser()).orElse(null));
      comment.setId(entity.getId());
      comment.setTimestamp(entity.getTimestamp());
      comment.setReplies(
          entity.getReplies().stream()
              .map(this::createResponse)
              .map(Optional::get)
              .collect(Collectors.toList()));
      return Optional.of(comment);
    }
    return Optional.empty();
  }
}
