package com.docmind.docmind_api.kafka;

import com.docmind.docmind_api.kafka.event.DocumentUploadedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor

public class DocumentEventProducer {
    private final KafkaTemplate<String, DocumentUploadedEvent> kafkaTemplate;

    public void publishDocumentUploadedEvent(DocumentUploadedEvent event){
        String topic_name = "document-uploaded";
       kafkaTemplate.send(topic_name, event);
    }
}
