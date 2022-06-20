package com.wutanda.napollo.managers.accountusers;

import com.wutanda.napollo.api.v1_0.accountusers.AccountUserRequest;
import com.wutanda.napollo.api.v1_0.accountusers.EmailActivationRequest;
import com.wutanda.napollo.api.v1_0.accountusers.MsisdnActivationRequest;
import com.wutanda.napollo.api.v1_0.accountusers.common.AccountUser;
import com.wutanda.napollo.common.authorization.Authorization;
import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.common.managers.AbstractManager;
import com.wutanda.napollo.common.transport.AbstractResponse;
import org.springframework.web.multipart.MultipartFile;

public interface AccountUserManager extends AbstractManager<AccountUserRequest, AccountUser> {

  AbstractResponse upgradeAccountUser(
      Authorization authorization, String accountUserIdentity, String accountUserType)
      throws NapolloException;

  AbstractResponse approveUpgradeAccountUser(
      Authorization authorization,
      String accountUserIdentity,
      String profileIdentity,
      Boolean approvalStatus)
      throws NapolloException;

  AbstractResponse<AccountUser> getAccountUser(final Authorization authorization)
      throws NapolloException;

  AbstractResponse updateAccountUser(
      final Authorization authorization, final AccountUserRequest accountUserRequest)
      throws NapolloException;

  AbstractResponse emailActivation(
      Authorization authorization, final EmailActivationRequest emailActivationRequest)
      throws NapolloException;

  AbstractResponse msisdnActivation(
      Authorization authorization, final MsisdnActivationRequest msisdnActivationRequest)
      throws NapolloException;

  AbstractResponse resendEmailActivationCode(
      Authorization authorization, final EmailActivationRequest emailActivationRequest)
      throws NapolloException;

  AbstractResponse resendMsisdnActivationCode(
      Authorization authorization, final MsisdnActivationRequest msisdnActivationRequest)
      throws NapolloException;

  AbstractResponse assignAccountUserGenre(
      Authorization authorization,
      final String accountUserIdentity,
      final String genreIdentity,
      Boolean assigned)
      throws NapolloException;

  AbstractResponse followAccountUser(
      Authorization authorization, final String followAccountUserIdentity, Boolean followState)
      throws NapolloException;

  AbstractResponse updateProfilePicture(Authorization authorization, MultipartFile multipartFile)
      throws NapolloException;
}
