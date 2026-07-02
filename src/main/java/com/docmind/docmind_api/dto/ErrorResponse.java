package com.docmind.docmind_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@RequiredArgsConstructor
@AllArgsConstructor

public class ErrorResponse {
    private Integer status;
    private String message;
    private LocalDateTime timestamp;

}
