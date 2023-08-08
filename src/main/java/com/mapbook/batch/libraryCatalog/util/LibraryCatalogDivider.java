package com.mapbook.batch.libraryCatalog.util;

import com.mapbook.batch.vo.LoanCntVo;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

@Slf4j
public class LibraryCatalogDivider {

    private static final int ISBN_IDX = 0;
    private static final int LOAN_CNT_IDX = 1;
    private static final String DEFAULT_OUTPUT_FORMAT = "%s_%d.csv";

    public static void main(String[] args) {

        LibraryCatalogDivider.divide("input\\loanCnt.csv",
            "mergeLoanCnt\\divide",
            10000000);
    }


    // 큰 용량의 CSV 파일을 분할 작업하기 위한 나누기 메소드
//    @BatchLogging
    public static Path divide(String path, String outPutNm, long maxRecordsPerFile) {

        long recordCount = 0; //몇개의 raws를 기록 했는지 저장.
        int fileCount = 0; // 나뉘어지는 file 이름에 해당 count를 붙여 file_3의 형식의 file 이름을 만듦

        try (Reader reader = Files.newBufferedReader(new File(path).toPath())) {

            CSVParser csvParser = CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .parse(reader);

            while (csvParser.iterator().hasNext()) {
                try (BufferedWriter writer = constructBufferedWriter(outPutNm, fileCount)) {
                    while (csvParser.iterator().hasNext() && !isEndLine(++recordCount, maxRecordsPerFile)) {
                        CSVRecord record = csvParser.iterator().next();

                        writer.write(buildCsvLine(record.get(ISBN_IDX), record.get(LOAN_CNT_IDX)));
                        writer.newLine();
                    }
                }
                fileCount++;
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return Path.of(outPutNm);
    }


    // 만약 maxRecord 만큼씩 나눠 파일에 담기 위해
    private static boolean isEndLine(long recordCount, long maxRecordsPerFile) {
        return recordCount % maxRecordsPerFile == 0;
    }

    private static LoanCntVo constructLoanCntVo(CSVRecord record) {
        return new LoanCntVo(
            record.get(ISBN_IDX),
            record.get(LOAN_CNT_IDX)
        );
    }

    private static BufferedWriter constructBufferedWriter(String outPutNm, int fileCount)
        throws IOException {

        String formattingFileNm = String.format(DEFAULT_OUTPUT_FORMAT, outPutNm, fileCount);

        return Files.newBufferedWriter(
            Paths.get(formattingFileNm),
            StandardCharsets.UTF_8
        );
    }

    // csv 파일로 다시 만들기 위해 "," 구분자를 넣어 준다.
    private static String buildCsvLine(String... args) {
        return String.join(",", args);
    }

}
