package com.wutanda.napollo.persistence.location.dao;

import com.wutanda.napollo.common.persistence.BaseDao;
import com.wutanda.napollo.persistence.location.LocationEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationEntityDao extends BaseDao<LocationEntity> {

  Optional<LocationEntity> findByUser_Id(String id);
}
