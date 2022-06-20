package com.wutanda.napollo.persistence.music.dao;

import com.wutanda.napollo.common.persistence.BaseDao;
import com.wutanda.napollo.persistence.music.PlaylistEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaylistEntityDao extends BaseDao<PlaylistEntity> {

  Page<PlaylistEntity> findByAccountUser_User_EmailAddress(String emailAddress, Pageable pageable);

  Optional<PlaylistEntity> findByNameAndAccountUser_User_EmailAddress(
      String name, String emailAddress);

  Page<PlaylistEntity> findByVisibleTrue(Pageable pageable);

  Page<PlaylistEntity> findByVisible(Boolean visible, Pageable pageable);
}
