package com.mapbook.batch.bookUpdateBatch.repository;

import com.mapbook.batch.bookUpdateBatch.entity.RequiredUpdateBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequiredUpdateBookRepo extends JpaRepository<RequiredUpdateBook,Long> {
    long countByNotFound(boolean notFound);
}
