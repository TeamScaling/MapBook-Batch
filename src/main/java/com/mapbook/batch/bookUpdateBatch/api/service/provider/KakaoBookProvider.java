package com.mapbook.batch.bookUpdateBatch.api.service.provider;

import com.mapbook.batch.bookUpdateBatch.api.apiConnection.ApiConnection;
import com.mapbook.batch.bookUpdateBatch.api.apiConnection.KakaoBookConn;
import com.mapbook.batch.bookUpdateBatch.api.apiConnection.OpenApi;
import com.mapbook.batch.bookUpdateBatch.api.service.AuthKeyLoader;
import com.mapbook.batch.bookUpdateBatch.api.util.ApiQueryBinder;
import com.mapbook.batch.bookUpdateBatch.api.util.ApiQuerySender;
import com.mapbook.batch.bookUpdateBatch.dto.BookApiDto;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class KakaoBookProvider implements DataProvider<BookApiDto> {

    private final ApiQuerySender apiQuerySender;
    private final ApiQueryBinder<BookApiDto> apiQueryBinder;

    private final AuthKeyLoader authKeyLoader;

    @PostConstruct
    public void loadAuthKey() {
        String apiAuthKey = authKeyLoader.loadAuthKey(OpenApi.KAKAO_BOOK).getAuthKey();
        KakaoBookConn.setApiAuthKey(apiAuthKey);
    }

    @Override
    public List<BookApiDto> provideDataList(List<? extends ApiConnection> connections, int nThreads) {

        List<ResponseEntity<String>> responseEntities = apiQuerySender.sendMultiQuery(
            connections,
            nThreads,
            new KakaoBookConn("", 1L).getHttpEntity()
        );

        return apiQueryBinder.bindList(responseEntities, this.getClass());
    }

}
