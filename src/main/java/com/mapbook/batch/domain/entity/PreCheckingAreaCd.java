package com.mapbook.batch.domain.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;

@Entity
@Getter @Table(name = "hasbook_area")
public class PreCheckingAreaCd {

    @Id
    private Integer areaCd;

}
