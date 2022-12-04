package ufrn.imd.jv.springcomment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CommentsService {

    @Value("${service.user}")
    private String userService;

    @Value("${service.issue}")
    private String issueService;
    private final CommentRepository repository;

    private final RestTemplate restTemplate;

    @Autowired
    public CommentsService(CommentRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    public boolean entidadeEhValida(String path, Long id) {
        ResponseEntity<String> response = restTemplate.exchange(
                path+"/"+id,
                HttpMethod.GET,
                null,
                String.class);
        return response.getStatusCode().is2xxSuccessful();
    }

    public CommentEntity save(CommentEntity commentEntity) {
        if(commentEntity == null) {
            throw new RuntimeException("Entidade não informada");
        }

        if (commentEntity.getUserId() == null) {
            throw new RuntimeException("Usuário não informado");
        }
        if(!entidadeEhValida(userService, commentEntity.getUserId())) {
            throw new RuntimeException("Usuário informado não existe");
        }

        if (commentEntity.getIssueId() == null) {
            throw new RuntimeException("Issue não informada");
        }
        if(!entidadeEhValida(issueService, commentEntity.getIssueId())) {
            throw new RuntimeException("Issue informada não existe");
        }

        if (commentEntity.getContent() == null) {
            throw new RuntimeException("Conteudo do comentário não informado");
        }
        if(!commentEntity.getContent().trim().equals("")) {
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
