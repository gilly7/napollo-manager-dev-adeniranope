package com.wutanda.napollo;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAdminServer
@SpringBootApplication
@EnableTransactionManagement
@EnableJpaRepositories
@EnableScheduling
public class Napollo {

  public static void main(String[] args) {
    SpringApplication.run(Napollo.class, args);
  }
}
