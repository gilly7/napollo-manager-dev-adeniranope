package com.wutanda.napollo.managers.loggers.beans;

import com.wutanda.napollo.common.exception.NapolloException;
import com.wutanda.napollo.common.loggers.LogLevel;
import com.wutanda.napollo.managers.loggers.LoggerManager;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class LoggerManagerBean implements LoggerManager {

  @Override
  public void log(Class<?> clazz, LogLevel logLevel, String logMessage) throws NapolloException {
    final Logger logger = LogManager.getLogger(clazz);
    switch (logLevel) {
      case INFO:
        logger.info(logMessage);
        break;
      case DEBUG:
        logger.debug(logMessage);
        break;
      case TRACE:
        logger.trace(logMessage);
        break;
      case ERROR:
        logger.error(logMessage);
        break;
      default:
        logger.log(Level.ALL, logMessage);
    }
  }

  @Override
  public void log(Class<?> clazz, LogLevel logLevel, String logMessage, Throwable throwable)
      throws NapolloException {
    final Logger logger = LogManager.getLogger(clazz);
    switch (logLevel) {
      case INFO:
        logger.info(logMessage, throwable);
        break;
      case DEBUG:
        logger.debug(logMessage, throwable);
        break;
      case TRACE:
        logger.trace(logMessage, throwable);
        break;
      case ERROR:
        logger.error(logMessage, throwable);
        break;
      default:
        logger.log(Level.ALL, logMessage, throwable);
    }
  }
}
