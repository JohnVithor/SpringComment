package ufrn.imd.jv.springcomment;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_gen")
    @SequenceGenerator(name = "comment_gen", sequenceName = "comment_seq", allocationSize = 1)
    private Long id;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @NotBlank
    private String content;

    @NotNull
    private Long issueId;

    @NotNull
    private Long userId;

    public CommentEntity() {
    }

    public CommentEntity(Long id, String content, Long issueId, Long userId) {
        this.id = id;
        this.createdAt = LocalDateTime.now();
        this.content = content;
        this.issueId = issueId;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String description) {
        this.content = description;
    }

    public Long getIssueId() {
        return issueId;
    }

    public void setIssueId(Long ownerId) {
        this.issueId = ownerId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
