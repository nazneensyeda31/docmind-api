package com.docmind.docmind_api.service;

import com.docmind.docmind_api.domain.entity.Document;
import com.docmind.docmind_api.domain.entity.DocumentStatus;
import com.docmind.docmind_api.domain.entity.User;
import com.docmind.docmind_api.dto.DocumentResponse;
import com.docmind.docmind_api.dto.DocumentUploadRequest;
import com.docmind.docmind_api.exception.ResourceNotFoundException;
import com.docmind.docmind_api.kafka.DocumentEventProducer;
import com.docmind.docmind_api.kafka.event.DocumentUploadedEvent;
import com.docmind.docmind_api.repository.DocumentRepository;
import com.docmind.docmind_api.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor

public class DocumentService {
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final PdfExtractorService pdfExtractorService;
    private final DocumentEventProducer eventProducer;
    private final ClaudeAiService claudeAiService;

    public DocumentResponse uploadDocument(MultipartFile file, UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(()->
                new ResourceNotFoundException("User Not Found with id: "+ userId));

        String extractedText = pdfExtractorService.extractText(file);



        Document document = Document.builder().fileName(file.getOriginalFilename())
                .fileSize(file.getSize())
                .extractedText(extractedText)
                .status(DocumentStatus.UPLOADED)
                .user(user)
                .build();

        Document saved = documentRepository.saveAndFlush(document);
        DocumentUploadedEvent event = DocumentUploadedEvent.builder()
                .documentId(saved.getId())
                .userId(saved.getUser().getId())
                .fileName(saved.getFileName())
                .extractedText(saved.getExtractedText())
                .build();
        eventProducer.publishDocumentUploadedEvent(event);






        return DocumentResponse.builder()
                .id(saved.getId())
                .fileName(saved.getFileName())
                .fileSize(saved.getFileSize())
                .extractedText(saved.getExtractedText())
                .aiSummary(saved.getAiSummary())
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
                .extractedText(document.getExtractedText())
                .aiSummary(document.getAiSummary())
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
                        .extractedText(doc.getExtractedText())
                        .aiSummary(doc.getAiSummary())
                        .status(doc.getStatus())
                        .uploadedAt(doc.getUploadedAt())
                        .userId(doc.getUser().getId()).build()).toList();
    }

    public String askQuestion(UUID documentId, String question){
        Document document = documentRepository.findById(documentId).orElseThrow(()->
                new ResourceNotFoundException("Document not found with id: "+ documentId));
        if(document.getStatus() != DocumentStatus.PROCESSED){
            throw new IllegalStateException("Document is not yet processed");
        }
            return claudeAiService.answerQuestion(document.getExtractedText(), question);

    }
}
