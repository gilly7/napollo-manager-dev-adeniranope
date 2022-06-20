package com.wutanda.napollo.persistence.music.dao;

import com.wutanda.napollo.common.persistence.BaseDao;
import com.wutanda.napollo.persistence.music.PlayHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayHistoryEntityDao extends BaseDao<PlayHistoryEntity> {

  Optional<Page<PlayHistoryEntity>> findByAccountUser_User_Id(
      String userIdentity, Pageable pageable);

  Optional<Page<PlayHistoryEntity>> findByMedia_Id(String mediaIdentity, Pageable pageable);

  List<PlayHistoryEntity> findByMedia_Id(String mediaIdentity);

  Optional<PlayHistoryEntity> findByMedia_IdAndAccountUser_User_Id(
      String mediaIdentity, String userIdentity);
}
