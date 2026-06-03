package com.docmind.docmind_api.dto;

import com.docmind.docmind_api.domain.entity.DocumentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class DocumentResponse {
    private UUID id;
    private String fileName;
    private Long fileSize;
    private String extractedText;
    private DocumentStatus status;
    private LocalDateTime uploadedAt;
    private UUID userId;
}
