package com.mapbook.batch.libraryCatalogBatch.batchConfig;

import com.mapbook.batch.libraryCatalogBatch.chunck.LibraryCatalogWriter;
import com.mapbook.batch.libraryCatalogBatch.domain.LibraryCatalog;
import com.mapbook.batch.libraryCatalogBatch.task.DownLoadFileClearTask;
import com.mapbook.batch.libraryCatalogBatch.util.CsvFileMerger;
import com.mapbook.batch.libraryCatalogBatch.util.LibraryCatalogAggregator;
import com.mapbook.batch.libraryCatalogBatch.util.LibraryCatalogNormalizer;
import com.mapbook.batch.libraryCatalogBatch.util.download.LibraryCatalogDownloader;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@RequiredArgsConstructor
@Configuration
public class LoanCntUpdateBatchConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final LibraryCatalogDownloader libraryCatalogDownloader;
    private final LibraryCatalogWriter libraryCatalogWriter;
    private final DownLoadFileClearTask downLoadFileClearTask;

    private static final int GROUP_SIZE = 100;

    @Bean
    public Job libraryCatalogBatch() {
        return jobBuilderFactory.get("updateLoanCntJob")
            .start(downloadStep(null))
            .next(normalizeStep())
            .next(aggregateStep())
            .next(mergeStep())
            .next(endAggregateStep())
            .next(updateToBookStep())
            .next(fileClearStep())
            .build();
    }

    @Bean
    @JobScope
    public Step downloadStep(@Value("#{jobParameters['downLoadDate']}") String downLoadDate) {
        return stepBuilderFactory.get("downloadStep")
            .tasklet((contribution, chunkContext) -> {
                libraryCatalogDownloader.downLoad(
                    "pipe/download",
                    downLoadDate.replaceAll("\"", ""),
                    false,
                    -1
                );
                return RepeatStatus.FINISHED;
            }).build();
    }

    @Bean
    public Step normalizeStep() {
        return stepBuilderFactory.get("normalizeStep")
            .tasklet((contribution, chunkContext) -> {
                LibraryCatalogNormalizer.normalize("pipe/download", "pipe/normalizeStep");
                return RepeatStatus.FINISHED;
            }).build();
    }

    @Bean
    public Step aggregateStep() {
        return stepBuilderFactory.get("aggregateStep")
            .tasklet((contribution, chunkContext) -> {
                LibraryCatalogAggregator.aggregateLoanCnt(
                    "pipe/normalizeStep",
                    "pipe/aggregatingStep/aggregate",
                    GROUP_SIZE);
                return RepeatStatus.FINISHED;
            }).build();
    }

    @Bean
    public Step mergeStep() {
        return stepBuilderFactory.get("mergeStep")
            .tasklet((contribution, chunkContext) -> {
                CsvFileMerger.mergeCsvFile(
                    "pipe/aggregatingStep",
                    "pipe/mergingStep/mergeFile",
                    null,
                    0, 1);
                return RepeatStatus.FINISHED;
            }).build();
    }

    @Bean
    public Step endAggregateStep() {
        return stepBuilderFactory.get("endAggregateStep")
            .tasklet((contribution, chunkContext) -> {
                LibraryCatalogAggregator.aggregateLoanCnt(
                    "pipe/mergingStep",
                    "pipe/endStep/end",
                    GROUP_SIZE);
                return RepeatStatus.FINISHED;
            }).build();
    }

    @Bean
    public Step updateToBookStep() {
        return stepBuilderFactory.get("updateLoanCnt2BookStep")
            .<LibraryCatalog, LibraryCatalog>chunk(100000)
            .reader(aggregatedFileReader())
            .writer(libraryCatalogWriter)
            .taskExecutor(new SimpleAsyncTaskExecutor())
            .build();
    }

    @Bean
    public FlatFileItemReader<LibraryCatalog> aggregatedFileReader() {
        return new FlatFileItemReaderBuilder<LibraryCatalog>()
            .name("flatFileLibCatalog")
            .resource(new FileSystemResource("pipe/endStep/end_0.csv"))
            .delimited().delimiter(",")
            .names("isbn", "loanCnt")
            .targetType(LibraryCatalog.class)
            .linesToSkip(1)
            .build();
    }

    @Bean
    public Step fileClearStep() {
        return stepBuilderFactory.get("clearStep")
            .tasklet(downLoadFileClearTask)
            .build();
    }

}
