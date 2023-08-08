package com.mapbook.batch.bookUpdateBatch.api.apiConnection;

import com.mapbook.batch.bookUpdateBatch.entity.RequiredUpdateBook;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.web.util.UriComponentsBuilder;

public class KakaoBookConn implements ApiConnection {

    private final Long id;
    private static final String API_URL = "https://dapi.kakao.com/v3/search/book";

    private static String API_AUTH_KEY;

    private final String target;
    public KakaoBookConn(String target,Long id) {
        this.id = id;
        this.target = target;
    }

    public KakaoBookConn(@NonNull RequiredUpdateBook updateBook) {
        this.id = updateBook.getId();
        this.target = updateBook.getIsbn();
    }

    @Override
    public UriComponentsBuilder configUriBuilder() {
        return UriComponentsBuilder.fromHttpUrl(API_URL)
            .queryParam("query", target);
    }
    public HttpEntity<String> getHttpEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", API_AUTH_KEY);

        return new HttpEntity<>("",headers);
    }

    public static void setApiAuthKey(String apiAuthKey) {
        API_AUTH_KEY = apiAuthKey;
    }
}
