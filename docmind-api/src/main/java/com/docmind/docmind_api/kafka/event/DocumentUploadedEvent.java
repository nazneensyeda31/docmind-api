package com.docmind.docmind_api.kafka.event;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor

public class DocumentUploadedEvent {
    private UUID documentId;
    private UUID userId;
    private String fileName;
    private String extractedText;
}
