package com.mapbook.batch.bookUpdateBatch.api.service;

import com.mapbook.batch.bookUpdateBatch.api.apiConnection.OpenApi;
import com.mapbook.batch.bookUpdateBatch.api.entity.AuthKey;
import com.mapbook.batch.bookUpdateBatch.api.repository.AuthKeyRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthKeyLoader {

    private final AuthKeyRepository authKeyRepo;

    public AuthKey loadAuthKey(@NonNull OpenApi openApi){

        return authKeyRepo.findById(openApi.getId())
            .orElseThrow(IllegalArgumentException::new);
    }

    public boolean checkAuthKey(OpenApi openApi,String authKey){

        return authKeyRepo.findById(openApi.getId())
            .map(storedKey -> storedKey.getAuthKey().equals(authKey))
            .orElseThrow(IllegalAccessError::new);
    }

}
