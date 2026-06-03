package com.docmind.docmind_api.service;

import com.docmind.docmind_api.domain.entity.Document;
import com.docmind.docmind_api.domain.entity.DocumentStatus;
import com.docmind.docmind_api.domain.entity.User;
import com.docmind.docmind_api.dto.DocumentResponse;
import com.docmind.docmind_api.dto.DocumentUploadRequest;
import com.docmind.docmind_api.exception.ResourceNotFoundException;
import com.docmind.docmind_api.repository.DocumentRepository;
import com.docmind.docmind_api.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor

public class DocumentService {
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;

    public DocumentResponse uploadDocument(DocumentUploadRequest request) {
        User user = userRepository.findById(request.getUserId()).orElseThrow(()->
                new ResourceNotFoundException("User Not Found with id: "+ request.getUserId()));

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

    public DocumentResponse getDocumentById(UUID id){
        Document document = documentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                "Document not found: "+ id
        ));


        return DocumentResponse.builder()
                .id(document.getId())
                .fileName(document.getFileName())
                .fileSize(document.getFileSize())
                .status(document.getStatus())
                .uploadedAt(document.getUploadedAt())
                .userId(document.getUser().getId())
                .build();
    }

    public List<DocumentResponse> getDocumentsById(UUID userId){
        User user = userRepository.findById(userId).orElseThrow(()->
                new ResourceNotFoundException(("User not found with id: "+ userId)));

        return documentRepository.findAllByUser_Id(userId)
                .stream()
                .map(doc -> DocumentResponse.builder()
                        .id(doc.getId())
                        .fileName(doc.getFileName())
                        .fileSize(doc.getFileSize())
                        .status(doc.getStatus())
                        .uploadedAt(doc.getUploadedAt())
                        .userId(doc.getUser().getId()).build()).toList();
    }
}
