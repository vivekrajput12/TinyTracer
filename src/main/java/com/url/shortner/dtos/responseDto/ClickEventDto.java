package com.url.shortner.dtos.responseDto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ClickEventDto {
    private LocalDateTime ClickDate;
    private long count;
}
