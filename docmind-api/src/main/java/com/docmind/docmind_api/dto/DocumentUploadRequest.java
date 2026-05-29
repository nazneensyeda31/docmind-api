package com.docmind.docmind_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class DocumentUploadRequest {
    @NotBlank
    private String fileName;
    @NotNull
    private Long fileSize;
    @NotNull
    private UUID userId;
}
