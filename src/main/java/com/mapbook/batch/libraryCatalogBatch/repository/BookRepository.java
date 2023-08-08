package com.mapbook.batch.libraryCatalogBatch.repository;


import com.mapbook.batch.libraryCatalogBatch.domain.Book;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book,Long> {


    List<Book> findBookByIsbnIn(Collection<String> isbn);
}

