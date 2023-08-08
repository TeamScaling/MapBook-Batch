package com.mapbook.batch.bookUpdateBatch.chunk;

import com.mapbook.batch.bookUpdateBatch.api.apiConnection.KakaoBookConn;
import com.mapbook.batch.bookUpdateBatch.api.service.provider.DataProvider;
import com.mapbook.batch.bookUpdateBatch.dto.BookApiDto;
import com.mapbook.batch.bookUpdateBatch.entity.RequiredUpdateBook;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class BookUpdateWriter implements ItemWriter<RequiredUpdateBook> {

    private final DataProvider<BookApiDto> kakaoBookProvider;

    @Override
    @Transactional
    public void write(List<? extends RequiredUpdateBook> items) {

        Map<String, RequiredUpdateBook> bookMap = items.stream()
            .collect(Collectors.toMap(RequiredUpdateBook::getIsbn, Function.identity()));

        List<KakaoBookConn> kakaoBookConns = items.stream().map(KakaoBookConn::new).toList();

        List<BookApiDto> bookApiDtoList = kakaoBookProvider.provideDataList(
            kakaoBookConns,
            kakaoBookConns.size()
        );

        updateBookEntity(bookApiDtoList, bookMap);
    }

    private void updateBookEntity(@NonNull List<BookApiDto> bookApiDtoList,
        Map<String, RequiredUpdateBook> bookMap) {

        bookApiDtoList.stream()
            .filter(bookApiDto -> bookMap.containsKey(bookApiDto.getIsbn()))
            .forEach(bookApiDto -> {
                RequiredUpdateBook requiredUpdateBook = bookMap.get(bookApiDto.getIsbn());
                requiredUpdateBook.updateData(bookApiDto);
                bookMap.remove(bookApiDto.getIsbn());
            });

        bookMap.values().forEach(requiredUpdateBook -> requiredUpdateBook.setNotFound(true));
    }
}
