package com.wutanda.napollo.managers.music;

import com.wutanda.napollo.api.v1_0.music.CommentRequest;
import com.wutanda.napollo.api.v1_0.music.common.Comment;
import com.wutanda.napollo.common.authorization.Authorization;
import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.common.managers.AbstractManager;
import com.wutanda.napollo.common.transport.AbstractResponse;
import org.springframework.data.domain.Page;

public interface CommentManager extends AbstractManager<CommentRequest, Comment> {

  AbstractResponse<Page<Comment>> mediaComments(
      Authorization authorization, String mediaIdentity, Integer page, Integer size)
      throws NapolloException;
}
