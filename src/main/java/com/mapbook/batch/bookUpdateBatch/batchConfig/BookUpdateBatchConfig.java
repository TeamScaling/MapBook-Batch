package com.mapbook.batch.bookUpdateBatch.batchConfig;

import com.mapbook.batch.bookUpdateBatch.chunk.BookUpdateWriter;
import com.mapbook.batch.bookUpdateBatch.entity.RequiredUpdateBook;
import javax.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
@RequiredArgsConstructor
public class BookUpdateBatchConfig {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final EntityManagerFactory entityManagerFactory;

    private final BookUpdateWriter bookUpdateWriter;

    private final int CHUNK_SIZE = 1000;
    private final int MAX_ITEM_SIZE = 10000;

    @Bean
    public Job bookUpdateJob() {
        return jobBuilderFactory.get("updateJob")
            .start(updateStep())
            .incrementer(new RunIdIncrementer())
            .build();
    }

    @Bean
    public Step updateStep() {

        return stepBuilderFactory.get("updateStep")
            .<RequiredUpdateBook, RequiredUpdateBook>chunk(CHUNK_SIZE)
            .reader(jpaPagingItemReader())
            .writer(bookUpdateWriter)
            .build();
    }

    @Bean
    public JpaPagingItemReader<RequiredUpdateBook> jpaPagingItemReader() {
        return new JpaPagingItemReaderBuilder<RequiredUpdateBook>()
            .name("requiredUpdateJpaReader")
            .pageSize(CHUNK_SIZE)
            .entityManagerFactory(entityManagerFactory)
            .maxItemCount(MAX_ITEM_SIZE)
            .queryString("select b from RequiredUpdateBook b where b.notFound = false")
            .build();
    }

}
