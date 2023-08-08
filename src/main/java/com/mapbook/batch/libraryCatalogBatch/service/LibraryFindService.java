package com.mapbook.batch.libraryCatalogBatch.service;

import com.mapbook.batch.libraryCatalogBatch.dto.LibraryInfoDto;
import com.mapbook.batch.libraryCatalogBatch.repository.LibraryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
@Slf4j
public class LibraryFindService {

    private final LibraryRepository libraryRepo;

    public List<LibraryInfoDto> getAllLibraries() {

        return libraryRepo.findAll().stream()
            .map(LibraryInfoDto::new)
            .toList();
    }

    public List<LibraryInfoDto> getLibrariesWithLimit(int limit) {

        return libraryRepo.findAll(Pageable.ofSize(limit))
            .getContent().stream()
            .map(LibraryInfoDto::new)
            .toList();
    }


}
