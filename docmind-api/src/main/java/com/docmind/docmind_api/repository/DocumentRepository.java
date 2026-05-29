package com.docmind.docmind_api.repository;

import com.docmind.docmind_api.domain.entity.Document;
import com.docmind.docmind_api.domain.entity.DocumentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository

public interface DocumentRepository extends JpaRepository<Document, UUID> {

    List<Document> findAllByUser_Id(UUID userId);


    List<Document> findAllByStatus(DocumentStatus status);
}
