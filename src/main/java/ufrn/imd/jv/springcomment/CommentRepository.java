package ufrn.imd.jv.springcomment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    Page<CommentEntity> findByIssueIdIs(Long id, PageRequest pageRequest);

    boolean existsByUserId(Long id);

    boolean existsByIssueId(Long id);


}
