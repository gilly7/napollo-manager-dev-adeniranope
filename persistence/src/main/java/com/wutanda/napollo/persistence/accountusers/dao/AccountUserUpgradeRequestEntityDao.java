package com.wutanda.napollo.persistence.accountusers.dao;

import com.wutanda.napollo.common.persistence.BaseDao;
import com.wutanda.napollo.persistence.accountusers.AccountUserUpgradeRequestEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountUserUpgradeRequestEntityDao
    extends BaseDao<AccountUserUpgradeRequestEntity> {

  Page<AccountUserUpgradeRequestEntity> findByPendingApproval(
      Boolean pendingApproval, Pageable pageable);

  Optional<AccountUserUpgradeRequestEntity> findByAccountUser_Id(String id);
}
