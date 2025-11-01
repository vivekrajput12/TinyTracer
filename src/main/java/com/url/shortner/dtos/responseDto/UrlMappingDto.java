package com.url.shortner.dtos.responseDto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UrlMappingDto {
    private long uid;
    private long urlId;
    private String shortUrl;
    private String originalUrl;
    private LocalDateTime createdAt;
}