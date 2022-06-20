package com.wutanda.napollo.persistence.music.dao;

import com.wutanda.napollo.common.persistence.BaseDao;
import com.wutanda.napollo.persistence.music.MediaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MediaEntityDao extends BaseDao<MediaEntity> {

  Optional<Page<MediaEntity>> findByAccountUser_Id(String userIdentity, Pageable pageable);

  Optional<Page<MediaEntity>> findByAccountUser_User_Id(String userIdentity, Pageable pageable);

  Optional<MediaEntity> findByAccountUser_User_IdAndDiscoveredTrue(String userIdentity);

  Page<MediaEntity> findByDiscoveredTrue(Pageable pageable);
}
