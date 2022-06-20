package com.wutanda.napollo.persistence.timelinepost.dao;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.wutanda.napollo.common.persistence.BaseDao;
import com.wutanda.napollo.persistence.timelinepost.TimelinePostEntity;

@Repository
public interface TimelinePostEntityDao extends BaseDao<TimelinePostEntity> {

	Optional<Page<TimelinePostEntity>> findByAccountUser_User_Id(String userIdentity, Pageable pageable);

}
