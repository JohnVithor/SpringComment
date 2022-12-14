package ufrn.imd.jv.springcomment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CommentsService {
    private final CommentResilience resilience;
    private final CommentRepository repository;

    @Autowired
    public CommentsService(CommentResilience resilience, CommentRepository repository) {
        this.resilience = resilience;
        this.repository = repository;
    }

    public CommentEntity save(CommentEntity commentEntity) {
        if (commentEntity == null) {
            throw new RuntimeException("Entidade não informada");
        }
        if (commentEntity.getUserId() == null) {
            throw new RuntimeException("Usuário não informado");
        }
        if (!resilience.isUserValid(commentEntity.getUserId())) {
            throw new RuntimeException("Usuário informado não existe");
        }
        if (commentEntity.getIssueId() == null) {
            throw new RuntimeException("Issue não informada");
        }
        if (!resilience.isIssueValid(commentEntity.getIssueId())) {
            throw new RuntimeException("Issue informada não existe");
        }
        if (commentEntity.getContent() == null) {
            throw new RuntimeException("Conteudo do comentário não informado");
        }
        if (commentEntity.getContent().trim().equals("")) {
            throw new RuntimeException("Conteudo do comentário informado é inválido");
        }
        return repository.save(commentEntity);
    }

    public ResponseEntity<Page<CommentEntity>> getPage(int page, int limit) {
        return ResponseEntity.ok(repository.findAll(PageRequest.of(page, limit)));
    }

    public ResponseEntity<Page<CommentEntity>> getByIssueId(Long id, int page, int limit) {
        return ResponseEntity.ok(repository.findByIssueIdIs(id, PageRequest.of(page, limit)));
    }

    public ResponseEntity<CommentEntity> getById(Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).build());
    }
}
