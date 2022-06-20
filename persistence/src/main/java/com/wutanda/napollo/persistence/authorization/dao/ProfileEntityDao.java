package com.wutanda.napollo.persistence.authorization.dao;

import com.wutanda.napollo.common.persistence.BaseDao;
import com.wutanda.napollo.persistence.authorization.ProfileEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileEntityDao extends BaseDao<ProfileEntity> {

  Optional<ProfileEntity> findByName(final String name);

  Optional<List<ProfileEntity>> findByProfileType(final String profileType);
}
