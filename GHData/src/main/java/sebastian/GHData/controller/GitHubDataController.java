package sebastian.GHData.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import sebastian.GHData.service.GetReposData;

@Controller
@RequestMapping("/GitHub")
public class GitHubDataController {

    @Autowired
    private GetReposData getReposData;

    @GetMapping(value = "/getData/{username}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity processUsername(@PathVariable String username) {
        return getReposData.processStringToJson(username);
    }

}