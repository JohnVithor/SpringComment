package ufrn.imd.jv.springcomment;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@FeignClient("SPRINGISSUE")
public interface IssueServiceInterface {
    @RequestMapping(method = RequestMethod.GET, value = "/issues/{id}")
    ResponseEntity<Map<String, String>> getIssue(@PathVariable Long id);
}
