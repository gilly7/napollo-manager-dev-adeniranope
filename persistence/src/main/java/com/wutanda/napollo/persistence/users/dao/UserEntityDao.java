package com.wutanda.napollo.persistence.users.dao;

import com.wutanda.napollo.common.persistence.BaseDao;
import com.wutanda.napollo.persistence.users.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserEntityDao extends BaseDao<UserEntity> {

  Optional<UserEntity> findByEmailAddressAndPassword(String emailAddress, String password);

  Optional<UserEntity> findByEmailAddressOrUsernameAndPassword(
      String emailAddress, String username, String password);

  Optional<UserEntity> findByEmailAddress(String emailAddress);

  Optional<UserEntity> findByMobileNumber(String mobileNumber);

  Optional<UserEntity> findByEmailAddressAndEmailActivationToken(
      String emailAddress, String emailActivationToken);

  Optional<UserEntity> findByMobileNumberAndMsisdnActivationToken(
      String mobileNumber, String msisdnActivationToken);

  Optional<UserEntity> findByUsername(String username);

  Optional<UserEntity> findByPasswordToken(String passwordToken);
}
