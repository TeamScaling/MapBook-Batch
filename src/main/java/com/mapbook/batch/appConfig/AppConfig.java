package com.mapbook.batch.appConfig;

import com.mapbook.batch.bookUpdateBatch.api.util.ApiQuerySender;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.batch.BasicBatchConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableAspectJAutoProxy
@RequiredArgsConstructor
public class AppConfig {

    private final JobRegistry jobRegistry;

    private final BasicBatchConfigurer basicBatchConfigurer;

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

    @Bean
    public BeanPostProcessor jobRegistryBeanPostProcessor(){

        JobRegistryBeanPostProcessor beanPostProcessor = new JobRegistryBeanPostProcessor();
        beanPostProcessor.setJobRegistry(jobRegistry);

        return beanPostProcessor;
    }

    @Bean
    public SimpleJobLauncher simpleJobLauncher(){

        SimpleJobLauncher simpleJobLauncher = (SimpleJobLauncher)basicBatchConfigurer.getJobLauncher();
        simpleJobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());

        return simpleJobLauncher;
    }


}