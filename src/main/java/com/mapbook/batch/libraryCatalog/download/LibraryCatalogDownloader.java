package com.mapbook.batch.libraryCatalog.download;

import com.mapbook.batch.domain.LibraryFindService;
import com.mapbook.batch.domain.dto.LibraryInfoDto;
import com.mapbook.batch.libraryCatalog.util.LibraryCatalogUrlCrawler;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
@Slf4j
public class LibraryCatalogDownloader extends AbstractDownLoader {

    private final LibraryFindService libraryFindService;
    private static final String DEFAULT_EXTENSION = ".csv";

    @Override
    public Path downLoad(String outPutDirectory, String targetDate,boolean limitOption,int limit) {

        // csv file을 서버로부터 다운 받기 위해 SSL을 모두 신뢰 한다.
        setupTrustAllSSLContext();

        List<LibraryInfoDto> libraryInfoList = setUpLibraryInfoByOption(limitOption,limit);

        libraryInfoList.parallelStream()
            .forEach(
                library -> {
                    try {
                        Optional<String> url =
                            LibraryCatalogUrlCrawler.getDownloadUrl(library.getLibCd(), targetDate);

                        if (url.isPresent()) {
                            String fileNm = configureFileName(outPutDirectory, library.getLibNm(), targetDate);
                            downloadFile(url.get(),fileNm);
                        }
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                }
            );

        return Path.of(outPutDirectory);
    }


    private String configureFileName(String outPutDirectory, String libNm, String date) {
        return outPutDirectory + "/"
            + String.join(" ", libNm, date)
            + String.join(".", DEFAULT_EXTENSION);
    }

    private List<LibraryInfoDto> setUpLibraryInfoByOption(boolean limitOption,int limit){

        return limitOption? libraryFindService.getLibrariesWithLimit(limit):
            libraryFindService.getAllLibraries();
    }


}
