package com.wutanda.napollo.common.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseDao<T extends BaseEntity> extends JpaRepository<T, String> {}
