package com.mapbook.batch.appConfig;

import com.mapbook.batch.bookUpdateBatch.api.util.ApiQuerySender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableAspectJAutoProxy
public class AppConfig {
    

    @Bean
    public RestTemplate restTemplateTimeOut() {

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();

        factory.setConnectTimeout(2000);
        factory.setReadTimeout(3000);

        return new RestTemplate(factory);
    }

    @Bean
    public RestTemplate restTemplate() {

        return new RestTemplate();
    }

    @Bean
    public ApiQuerySender apiQuerySenderTimeOut() {
        return new ApiQuerySender(restTemplateTimeOut());
    }

    @Bean
    public ApiQuerySender apiQuerySender() {

        return new ApiQuerySender(restTemplate());
    }



}