package com.docmind.docmind_api.kafka;

import com.docmind.docmind_api.domain.entity.Document;
import com.docmind.docmind_api.domain.entity.DocumentStatus;
import com.docmind.docmind_api.exception.ResourceNotFoundException;
import com.docmind.docmind_api.kafka.event.DocumentUploadedEvent;
import com.docmind.docmind_api.repository.DocumentRepository;
import com.docmind.docmind_api.service.ClaudeAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class DocumentEventConsumer {
    private final ClaudeAiService claudeAiService;
    private final DocumentRepository documentRepository;
    @KafkaListener(topics = "document-uploaded")
    @Transactional
    public void documentConsume(DocumentUploadedEvent event){
        String summarizedText = claudeAiService.summarizeDocument(event.getExtractedText());
        Document document = documentRepository.findById(event.getDocumentId()).orElseThrow(() ->
                new ResourceNotFoundException("Document not Found by Id: "+ event.getDocumentId())
        );
        System.out.println("Claude Response: "+ summarizedText);
        document.setAiSummary(summarizedText);
        document.setStatus(DocumentStatus.PROCESSED);
        documentRepository.saveAndFlush(document);



    }

}
