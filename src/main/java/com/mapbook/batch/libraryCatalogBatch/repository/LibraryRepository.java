package com.mapbook.batch.libraryCatalogBatch.repository;

import com.mapbook.batch.libraryCatalogBatch.domain.LibraryInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryRepository extends JpaRepository<LibraryInfo, Integer> {

}
