package com.docmind.docmind_api.controller;

import com.docmind.docmind_api.dto.DocumentResponse;
import com.docmind.docmind_api.dto.DocumentUploadRequest;
import com.docmind.docmind_api.service.DocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor


public class DocumentController {
    private final DocumentService documentService;
    @PostMapping("/documents")
    public ResponseEntity<@Valid DocumentResponse> documentResponseEntity( @RequestBody DocumentUploadRequest request){
        DocumentResponse response = documentService.uploadDocument(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
