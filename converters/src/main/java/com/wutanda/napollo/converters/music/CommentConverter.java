package com.wutanda.napollo.converters.music;

import com.wutanda.napollo.api.v1_0.music.CommentRequest;
import com.wutanda.napollo.api.v1_0.music.common.Comment;
import com.wutanda.napollo.converters.Converter;
import com.wutanda.napollo.persistence.music.CommentEntity;

public interface CommentConverter extends Converter<CommentRequest, Comment, CommentEntity> {}
