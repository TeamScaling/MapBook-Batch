package com.mapbook.batch.vo;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class LoanCntVo {

    @CsvBindByName(column = "ISBN")
    private String isbn;
    // 책 제목
    @CsvBindByName(column = "LOAN_CNT")
    private String loanCnt;

    public LoanCntVo(String isbn, String loanCnt) {
        this.isbn = isbn;
        this.loanCnt = loanCnt;
    }


}
