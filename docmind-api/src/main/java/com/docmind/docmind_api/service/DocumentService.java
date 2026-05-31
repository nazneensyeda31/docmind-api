package com.docmind.docmind_api.service;

import com.docmind.docmind_api.domain.entity.Document;
import com.docmind.docmind_api.domain.entity.DocumentStatus;
import com.docmind.docmind_api.domain.entity.User;
import com.docmind.docmind_api.dto.DocumentResponse;
import com.docmind.docmind_api.dto.DocumentUploadRequest;
import com.docmind.docmind_api.repository.DocumentRepository;
import com.docmind.docmind_api.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor

public class DocumentService {
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;

    public DocumentResponse uploadDocument(DocumentUploadRequest request) {
        User user = userRepository.findById(request.getUserId()).orElseThrow(()->
                new RuntimeException("User Not Found"));

        Document document = Document.builder().fileName(request.getFileName())
                .fileSize(request.getFileSize())
                .status(DocumentStatus.UPLOADED)
                .user(user)
                .build();

        Document saved = documentRepository.saveAndFlush(document);

        return DocumentResponse.builder()
                .id(saved.getId())
                .fileName(saved.getFileName())
                .fileSize(saved.getFileSize())
                .status(saved.getStatus())
                .uploadedAt(saved.getUploadedAt())
                .userId(saved.getUser().getId())
                .build();
    }
}
