package ufrn.imd.jv.springcomment;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CommentResilience {
    private final UserServiceInterface userService;
    private final IssueServiceInterface issueService;
    private final CommentRepository repository;

    @Autowired
    public CommentResilience(UserServiceInterface userService,
                             IssueServiceInterface issueService,
                             CommentRepository repository) {
        this.userService = userService;
        this.issueService = issueService;
        this.repository = repository;
    }

    @CircuitBreaker(name = "isUserValid_cb", fallbackMethod = "isUserKnown")
    @Bulkhead(name = "isUserValid_bh", fallbackMethod = "isUserKnown")
    public boolean isUserValid(Long id) {
        ResponseEntity<Map<String, String>> response = userService.getUser(id);
        return response.getStatusCode().is2xxSuccessful();
    }

    @CircuitBreaker(name = "isIssueValid_cb", fallbackMethod = "isIssueKnown")
    @Bulkhead(name = "isIssueValid_bh", fallbackMethod = "isIssueKnown")
    public boolean isIssueValid(Long id) {
        ResponseEntity<Map<String, String>> response = issueService.getIssue(id);
        return response.getStatusCode().is2xxSuccessful();
    }

    public boolean isUserKnown(Long id, Throwable t) {
        System.err.println(
                "Não foi possível consultar o service de usuários devido a: " +
                        t.getMessage() +
                        "Consultando comments locais em busca do usuário"
        );
        if (repository.existsByUserId(id)) {
            System.err.println("Usuário foi encontrado, portanto é válido");
            return true;
        } else {
            System.err.println("Não foi encontrado comment criado pelo usuário de id="+id);
            return false;
        }
    }

    public boolean isIssueKnown(Long id, Throwable t) {
        System.err.println(
                "Não foi possível consultar o service de issues devido a: " +
                        t.getMessage() +
                        "Consultando comments locais em busca da issue"
        );
        if (repository.existsByIssueId(id)) {
            System.err.println("Issue foi encontrado, portanto é válida");
            return true;
        } else {
            System.err.println("Não foi encontrado comment associado a issue de id="+id);
            return false;
        }
    }
}
