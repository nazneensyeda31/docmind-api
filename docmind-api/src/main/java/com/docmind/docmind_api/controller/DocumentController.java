package com.docmind.docmind_api.controller;

import com.docmind.docmind_api.dto.DocumentResponse;
import com.docmind.docmind_api.dto.DocumentUploadRequest;
import com.docmind.docmind_api.service.DocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor


public class DocumentController {
    private final DocumentService documentService;
    @PostMapping(value = "/documents", consumes = "multipart/form-data")
    public ResponseEntity<DocumentResponse> documentResponseEntity(@RequestParam MultipartFile file,
                                                                   @RequestParam UUID userId){
        DocumentResponse response = documentService.uploadDocument(file, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/documents/{id}")
    public  ResponseEntity<DocumentResponse> documentResponseEntityById(@Valid @PathVariable("id")UUID id){
        DocumentResponse response = documentService.getDocumentById(id);
        return ResponseEntity.ok().body(response);

    }

    @GetMapping("/documents")
    public ResponseEntity<List<DocumentResponse>> documentsResponseEntityByUserId(@RequestParam("userId") UUID id){
        List<DocumentResponse> documentResponses = documentService.getDocumentsById(id);
        return ResponseEntity.ok().body(documentResponses);
    }

}
