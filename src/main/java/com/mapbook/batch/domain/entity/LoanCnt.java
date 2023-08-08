package com.mapbook.batch.domain.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LOAN_CNT")
public class LoanCnt {

    @Id
    private String isbn;

    private int loanCnt;

    public LoanCnt() {
    }

    public LoanCnt(String isbn, int loanCnt) {
        this.isbn = isbn;
        this.loanCnt = loanCnt;
    }
}
