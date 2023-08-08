package com.mapbook.batch.libraryCatalog.chunck;

import com.mapbook.batch.libraryCatalog.domain.LibraryCatalog;
import com.mapbook.batch.domain.entity.Book;
import com.mapbook.batch.domain.entity.RequiredUpdateBook;
import com.mapbook.batch.domain.repository.BookRepository;
import com.mapbook.batch.domain.repository.RequiredUpdateBookRepo;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class LibraryCatalogWriter implements ItemWriter<LibraryCatalog> {

    private final BookRepository bookRepository;

    private final RequiredUpdateBookRepo requiredUpdateBookRepo;

    @Override
    @Transactional
    public void write(List<? extends LibraryCatalog> items) throws Exception {

        Map<String, Integer> libraryCatalogMap = items.stream()
            .collect(Collectors.toMap(
                LibraryCatalog::getIsbn,
                LibraryCatalog::getLoanCnt)
            );

        Set<String> isbnSet = libraryCatalogMap.keySet();
        List<Book> books = bookRepository.findBookByIsbnIn(isbnSet);

        books.forEach(book -> {
            Integer loanCnt = libraryCatalogMap.get(book.getIsbn());
            book.setLoanCnt(loanCnt);
            libraryCatalogMap.remove(book.getIsbn());
        });

        saveRequiredUpdateBook(libraryCatalogMap);
    }

    private void saveRequiredUpdateBook(Map<String, Integer> libraryCatalogMap) {

        List<RequiredUpdateBook> requiredUpdateBooks = libraryCatalogMap.entrySet().stream()
            .map(entry ->
                new RequiredUpdateBook(entry.getKey(), entry.getValue()))
            .toList();

        requiredUpdateBookRepo.saveAll(requiredUpdateBooks);
    }
}
