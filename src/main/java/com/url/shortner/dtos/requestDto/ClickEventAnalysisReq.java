package com.url.shortner.dtos.requestDto;

import com.url.shortner.constants.GroupBy;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class ClickEventAnalysisReq {
    @NotNull(message = "URL ID is required")
    private Long urlId;
    private String startDate;
    private String endDate;
    private GroupBy groupBy;
}
