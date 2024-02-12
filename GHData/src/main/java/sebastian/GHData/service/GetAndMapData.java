package sebastian.GHData.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sebastian.GHData.resource.repoBranchesRequester;
import sebastian.GHData.resource.repoListRequester;
import sebastian.GHData.response.external.RepoObject;
import sebastian.GHData.response.inner.UserNotFound;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetAndMapData {

    public ResponseEntity<String> processStringToJson(String username) {
        try {

            // request all user's repositories from GitHub API
            String jsonReposResponse = repoListRequester.sendGetRequest(username);

            assert jsonReposResponse != null;
            if (jsonReposResponse.equals("User not found")) {
                return userNotFound(jsonReposResponse);
            } else if (jsonReposResponse.equals("The limit of requests")) {
                return requestsLimit(jsonReposResponse);
            }

            ObjectMapper mapper = new ObjectMapper();

            List<RepoObject> modifiedRepos = mapper.readValue(jsonReposResponse, new TypeReference<List<RepoObject>>(){})
                    .stream()
                    .filter(repo -> repo.getFork().equals("false"))
                    .peek(repo -> repo.setFork(null))
                    .map(repo -> {
                        String repoResponse = repoBranchesRequester.sendGetRequest(repo.getOwnerLogin(), repo.getRepositoryName());
                        ObjectMapper mapperBranch = new ObjectMapper();
                        try {
                            JsonNode jsonBranches = mapperBranch.readTree(repoResponse);
                            HashMap<String, String> branchesMap = new HashMap<>();
                            for (JsonNode branch : jsonBranches) {
                                branchesMap.put(branch.get("name").asText(), branch.get("commit").get("sha").asText());
                            }
                            repo.setBranchesMap(branchesMap);
                            return repo;
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toList());


            // Converting data to JSON
            String jsonResponse = mapper.writeValueAsString(modifiedRepos);

            ResponseEntity<String> responseEntity = ResponseEntity.ok()
                    .header("Content-Type", "application/json")
                    .body(jsonResponse);

            return responseEntity;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error with converting JSON", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> userNotFound(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String notFoundResponse = mapper.writeValueAsString(new UserNotFound(404, json));

        ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND)
                .header("Content-Type", "application/json")
                .body(notFoundResponse);

        return responseEntity;
    }

    public ResponseEntity<String> requestsLimit(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String requestsLimitResponse = mapper.writeValueAsString(new UserNotFound(403, json));

        ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .header("Content-Type", "application/json")
                .body(requestsLimitResponse);

        return responseEntity;
    }

}
