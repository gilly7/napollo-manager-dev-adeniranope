package com.wutanda.napollo.managers.loggers;

import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.common.loggers.LogLevel;

public interface LoggerManager {

  void log(Class<?> clazz, LogLevel logLevel, String logMessage) throws NapolloException;

  void log(Class<?> clazz, LogLevel logLevel, String logMessage, Throwable throwable)
      throws NapolloException;
}
