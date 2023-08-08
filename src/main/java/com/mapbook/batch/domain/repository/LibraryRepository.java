package com.mapbook.batch.domain.repository;

import com.mapbook.batch.domain.entity.LibraryInfo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface LibraryRepository extends JpaRepository<LibraryInfo, Integer> {

    @Transactional(readOnly = true)
    List<LibraryInfo> findAllByAreaCd(Integer areaCd);

}
