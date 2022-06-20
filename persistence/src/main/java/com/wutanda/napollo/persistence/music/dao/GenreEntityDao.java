package com.wutanda.napollo.persistence.music.dao;

import com.wutanda.napollo.common.persistence.BaseDao;
import com.wutanda.napollo.persistence.music.GenreEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenreEntityDao extends BaseDao<GenreEntity> {

  Optional<GenreEntity> findByName(String name);

  Optional<Page<GenreEntity>> findByStatus(Boolean status, Pageable pageable);
}
