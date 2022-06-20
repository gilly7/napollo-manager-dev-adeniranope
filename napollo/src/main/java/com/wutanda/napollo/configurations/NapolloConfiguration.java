package com.wutanda.napollo.configurations;

import com.google.common.collect.ImmutableList;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class NapolloConfiguration {

  @Bean
  public RestTemplate restTemplate() {
    final RestTemplate restTemplate = new RestTemplate();
    restTemplate.setMessageConverters(
        ImmutableList.<HttpMessageConverter<?>>builder()
            .add(new MappingJackson2HttpMessageConverter())
            .add(new FormHttpMessageConverter())
            .add(new AllEncompassingFormHttpMessageConverter())
            .build());
    return restTemplate;
  }
}
