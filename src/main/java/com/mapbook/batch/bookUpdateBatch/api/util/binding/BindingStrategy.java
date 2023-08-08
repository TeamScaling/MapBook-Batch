package com.mapbook.batch.bookUpdateBatch.api.util.binding;

import com.mapbook.batch.bookUpdateBatch.exception.OpenApiException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public interface BindingStrategy<T> {

    T bind (ResponseEntity<String> apiResponse) throws OpenApiException;
}
