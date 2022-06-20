package com.wutanda.napollo.install.authorization;

import com.wutanda.napollo.common.exception.NapolloException;

public interface InstallManager {

  void createDefaultProfiles();

  void createSuperAdministrator() throws NapolloException;
}
