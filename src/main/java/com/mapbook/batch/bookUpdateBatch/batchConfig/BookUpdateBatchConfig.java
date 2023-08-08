package com.mapbook.batch.bookUpdateBatch.batchConfig;

import com.mapbook.batch.bookUpdateBatch.chunk.BookUpdateWriter;
import com.mapbook.batch.bookUpdateBatch.entity.RequiredUpdateBook;
import com.mapbook.batch.bookUpdateBatch.repository.RequiredUpdateBookRepo;
import javax.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BookUpdateBatchConfig {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final EntityManagerFactory entityManagerFactory;

    private final BookUpdateWriter bookUpdateWriter;

    private final RequiredUpdateBookRepo requiredUpdateBookRepo;

    private final int CHUNK_SIZE = 1000;
    private final int MAX_ITEM_SIZE = 30000;

    @Bean
    public Job bookUpdateJob() {
        return jobBuilderFactory.get("updateJob")
            .start(checkingStep())
                .on("NOT_REQUIRED_UPDATE")
                .stop()
                .on("*")
                .to(updateStep())
                .end()
            .incrementer(new RunIdIncrementer())
            .build();
    }

    @Bean
    public Step checkingStep() {
        return stepBuilderFactory.get("checkingStep")
            .tasklet((contribution, chunkContext) -> {
                if (requiredUpdateBookRepo.countByNotFound(false) == 0) {
                    contribution.getStepExecution().setExitStatus(
                        new ExitStatus("NOT_REQUIRED_UPDATE")
                    );
                }
                return RepeatStatus.FINISHED;
            }).build();
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
