package it.cgmconsulting.myblog.payload.request;

import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class ReportRequest {

    @Min(0)
    private int commentId;
    @Min(0)
    private int madeByYouId;
}
