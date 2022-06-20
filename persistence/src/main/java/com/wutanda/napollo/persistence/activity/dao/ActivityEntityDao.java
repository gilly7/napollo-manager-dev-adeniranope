package com.wutanda.napollo.persistence.activity.dao;

import com.wutanda.napollo.common.persistence.BaseDao;
import com.wutanda.napollo.persistence.activity.ActivityEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityEntityDao extends BaseDao<ActivityEntity> {

  Page<ActivityEntity> findByCreatedUser(String createdUser, Pageable pageable);
}
