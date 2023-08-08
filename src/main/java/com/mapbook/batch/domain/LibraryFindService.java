package com.mapbook.batch.domain;

import com.mapbook.batch.domain.dto.LibraryInfoDto;
import com.mapbook.batch.domain.repository.LibraryRepository;
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


    /**
     * 지역 코드 방식의 사용자 요청일 경우 주변 도서관을 찾아서 반환 한다.
     *
     * @param areaCd 특정 지역 단위에 대한 지역 코드
     * @return 지역 코드를 통해 찾은 주변 도서관 정보 Dto를 담은 List
     */
    public List<LibraryInfoDto> getNearByLibInfoByAreaCd(Integer areaCd) {
        log.info("This is not support Area");
        return libraryRepo.findAllByAreaCd(areaCd).stream()
            .map(LibraryInfoDto::new)
            .toList();
    }


    /**
     * 주어진 지역 코드가 소장 가능 도서관 서비스 지역이면 true를 반환 한다.
     *
     * @param areaCd 특정 지역 단위에 대한 지역 코드
     * @return areaCd를 지원 중인 도서관 코드 목록에서 찾을 수 있다면 true, 그렇지 않다면 false
     */
    private boolean isPreCheckingSupportedArea(Integer areaCd) {
        // 전처리 기능 삭제 예정
//        return hasBookAreaRepo.findById(areaCd).isPresent();
        return false;
    }

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
