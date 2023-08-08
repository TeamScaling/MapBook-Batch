package com.mapbook.batch.bookUpdateBatch.jobController;

import lombok.Getter;
import lombok.Setter;

@Getter
public class ReqJobDto {

    private String jobName;

    private String parameters;

    private String authKey;

    public ReqJobDto(String jobName, String parameters, String authKey) {
        this.jobName = jobName;
        this.parameters = parameters;
        this.authKey = authKey;
    }
}
