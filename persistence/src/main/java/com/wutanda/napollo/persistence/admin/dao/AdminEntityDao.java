package com.wutanda.napollo.persistence.admin.dao;

import com.wutanda.napollo.common.persistence.BaseDao;
import com.wutanda.napollo.persistence.admin.AdminEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminEntityDao extends BaseDao<AdminEntity> {

  Optional<AdminEntity> findByUser_username(String username);

  Optional<AdminEntity> findByUser_emailAddress(String emailAddress);
}
