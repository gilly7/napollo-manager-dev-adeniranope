package com.wutanda.napollo.persistence.accountusers.dao;

import com.wutanda.napollo.common.persistence.BaseDao;
import com.wutanda.napollo.persistence.accountusers.AccountUserEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountUserEntityDao extends BaseDao<AccountUserEntity> {

  Optional<AccountUserEntity> findByStageName(String stageName);

  Optional<AccountUserEntity> findByUser_Id(String id);

  Optional<AccountUserEntity> findByUser_EmailAddress(String emailAddress);

  Optional<AccountUserEntity> findByUser_MobileNumber(String mobileNumber);

  Optional<AccountUserEntity> findByUser_Username(String username);
}
