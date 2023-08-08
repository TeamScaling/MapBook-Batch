package com.mapbook.batch.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Entity
@Table(name = "REQUIRED_UPDATE_BOOK")
@Setter @Getter
public class RequiredUpdateBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ISBN")
    private String isbn;

    @Column(name = "LOAN_CNT")
    private int loanCnt;

    @Column(name = "TITLE_NM")
    @Nullable
    private String title;

    @Nullable
    @Column(name = "BOOK_INTRCN_CN")
    private String content;

    @Nullable
    @Column(name = "AUTHR_NM")
    private String author;

    @Nullable
    @Column(name = "IMAGE_URL")
    private String bookImg;

    @Nullable
    @Column(name = "pblicte_de")
    private String publishDate;

    public RequiredUpdateBook() {
    }

    public RequiredUpdateBook(String isbn, int loanCnt) {
        this.isbn = isbn;
        this.loanCnt = loanCnt;
    }
}
