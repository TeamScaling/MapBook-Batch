package com.mapbook.batch.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "book_final")
@ToString
@Getter @Setter
public class Book {

    @Id
    @Column(name = "id_no")
    private Long id;

    @Column(name = "TITLE_NM")
    private String title;

    @Column(name = "BOOK_INTRCN_CN")
    private String content;

    @Column(name = "AUTHR_NM")
    private String author;

    @Column(name = "ISBN_THIRTEEN_NO")
    private String isbn;

    @Column(name = "IMAGE_URL")
    private String bookImg;

    @Column(name = "title_token")
    private String titleToken;

    @Column(name = "pblicte_de")
    private String publishDate;

    @Column(name = "loan_cnt")
    private Integer loanCnt;


    public Integer getLoanCnt() {
        return loanCnt == null ? 0 : this.loanCnt;
    }
}
