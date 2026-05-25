package com.docmind.docmind_api.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "documents")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor


public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;
    @Column(nullable = false)
    private String fileName;
    private Long fileSize;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentStatus status;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime uploadedAt;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
