package com.wutanda.napollo.persistence.music.dao;

import com.wutanda.napollo.common.persistence.BaseDao;
import com.wutanda.napollo.persistence.music.PlayEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayEntityDao extends BaseDao<PlayEntity> {

  Optional<PlayEntity> findByMedia_IdAndCityAndStateAndCountry(
      String mediaIdentity, String city, String state, String country);

  Optional<Page<PlayEntity>> findByCityAndStateAndCountry(
      String city, String state, String country, Pageable pageable);

  List<PlayEntity> findByMedia_Id(String mediaIdentity);
}
