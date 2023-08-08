package com.mapbook.batch.domain.repository;


import com.mapbook.batch.domain.entity.Book;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book,Long> {

    Optional<Book> findBookByIsbn(String isbn);

    List<Book> findBookByIsbnIn(Collection<String> isbn);
}

