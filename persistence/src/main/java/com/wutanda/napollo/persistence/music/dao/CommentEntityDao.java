package com.wutanda.napollo.persistence.music.dao;

import com.wutanda.napollo.common.persistence.BaseDao;
import com.wutanda.napollo.persistence.music.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentEntityDao extends BaseDao<CommentEntity> {

  Optional<Page<CommentEntity>> findByAccountUser_User_IdAndReplyStateFalse(
      String userIdentity, Pageable pageable);

  Optional<Page<CommentEntity>> findByMedia_IdAndReplyStateFalse(
      String mediaIdentity, Pageable pageable);
}
