package sebastian.GHData.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sebastian.GHData.model.GitHubRepoData;
import sebastian.GHData.model.UserNotFound;
import sebastian.GHData.resource.repoBranchesRequester;
import sebastian.GHData.resource.repoListRequester;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class GetReposData {
    // Creating response logic

    public ResponseEntity<String> processStringToJson(String username) {
        try {

            // request all user's repositories from GitHub API
            String jsonResponse = repoListRequester.sendGetRequest(username);
            ObjectMapper objectMapper = new ObjectMapper();

            if (jsonResponse.equals("User not found")) {
                String notFoundResponse = objectMapper.writeValueAsString(new UserNotFound(404, jsonResponse));

                ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Content-Type", "application/json")
                        .body(notFoundResponse);

                return responseEntity;
            }

            if (jsonResponse.equals("The limit of requests for the GitHub API from this IP address has been reached")) {
                String requestsLimitResponse = objectMapper.writeValueAsString(new UserNotFound(403, jsonResponse));

                ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                        .header("Content-Type", "application/json")
                        .body(requestsLimitResponse);

                return responseEntity;
            }

            JsonNode repositories = objectMapper.readTree(jsonResponse);

            List<GitHubRepoData> repoList = new ArrayList<>();

            for (JsonNode repository : repositories) {
                // filtering of repositories that are forks
                if (repository.get("fork").asBoolean()) {
                    continue;
                }
                GitHubRepoData gitHubRepoData = new GitHubRepoData();
                gitHubRepoData.setRepositoryName(repository.get("name").asText());
                gitHubRepoData.setOwnerLogin(repository.get("owner").get("login").asText());

                // request all branches of repository from GitHub API
                String repoResponse = repoBranchesRequester.sendGetRequest(username, repository.get("name").asText());
                JsonNode jsonRepoNode = objectMapper.readTree(repoResponse);

                HashMap<String, String> branches = new HashMap<>();
                for (JsonNode repoNode : jsonRepoNode) {
                    branches.put(repoNode.get("name").asText(), repoNode.get("commit").get("sha").asText());
                }

                gitHubRepoData.setBranchData(branches);

                repoList.add(gitHubRepoData);
            }

            // Converting data to JSON
            String jsonString = objectMapper.writeValueAsString(repoList);

            ResponseEntity<String> responseEntity = ResponseEntity.ok()
                    .header("Content-Type", "application/json")
                    .body(jsonString);

            return responseEntity;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error with converting JSON", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
