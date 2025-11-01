package com.url.shortner.dtos.requestDto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MappingRequest {
    @NotNull(message = "original url cannot be empty")
    private String originalUrl;
    @NotNull(message = "UID cannot be null")
    private Long uid;
}
