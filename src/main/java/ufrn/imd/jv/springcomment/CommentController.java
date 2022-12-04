package ufrn.imd.jv.springcomment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "comments")
public class CommentController {

    private final CommentsService service;

    @Autowired
    public CommentController(CommentsService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CommentEntity> createIssue(@RequestBody CommentEntity commentEntity) {
        return ResponseEntity.ok(service.save(commentEntity));
    }

    @GetMapping
    public ResponseEntity<Page<CommentEntity>> getPage(
            @RequestParam(name = "pg", required = false) Optional<Integer> page,
            @RequestParam(name = "lim", required = false) Optional<Integer> limit) {
        return service.getPage(page.orElse(0), limit.orElse(10));
    }

    @GetMapping(path = "column/{id}")
    public ResponseEntity<Page<CommentEntity>> getBoardId(@PathVariable Long id,
                                                          @RequestParam(name = "pg", required = false) Optional<Integer> page,
                                                          @RequestParam(name = "lim", required = false) Optional<Integer> limit) {
        return service.getByIssueId(id, page.orElse(0), limit.orElse(10));
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<CommentEntity> getById(@PathVariable Long id) {
        return service.getById(id);
    }
}
