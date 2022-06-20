package com.wutanda.napollo.persistence.music.dao;

import com.wutanda.napollo.common.persistence.BaseDao;
import com.wutanda.napollo.persistence.music.AlbumEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlbumEntityDao extends BaseDao<AlbumEntity> {

  Optional<AlbumEntity> findByNameAndOwnerAccountUser_Id(String name, String userIdentity);

  Optional<AlbumEntity> findByNameAndOwnerAccountUser_User_Id(String name, String userIdentity);

  Optional<Page<AlbumEntity>> findByOwnerAccountUser_User_Id(
      String userIdentity, Pageable pageable);
}
