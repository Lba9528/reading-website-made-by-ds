package com.reading.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reading_progress",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "book_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReadingProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(name = "current_chapter_id")
    private Long currentChapterId;

    @Column(name = "last_read_at")
    private LocalDateTime lastReadAt;

    @PrePersist
    protected void onCreate() {
        lastReadAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        lastReadAt = LocalDateTime.now();
    }
}