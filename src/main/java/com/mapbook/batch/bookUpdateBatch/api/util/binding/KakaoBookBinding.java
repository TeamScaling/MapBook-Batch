package com.mapbook.batch.bookUpdateBatch.api.util.binding;

import com.mapbook.batch.bookUpdateBatch.dto.BookApiDto;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;

public class KakaoBookBinding implements BindingStrategy<BookApiDto> {


    @Override
    public BookApiDto bind(ResponseEntity<String> apiResponse) {

        if (apiResponse == null) {
            return null;
        }

        JSONArray jsonArray = new JSONObject(apiResponse.getBody()).getJSONArray("documents");

        return jsonArray.length() != 0
            ? new BookApiDto(jsonArray.getJSONObject(0)) : new BookApiDto();
    }
}
