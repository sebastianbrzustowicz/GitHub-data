package sebastian.GHData.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sebastian.GHData.resource.RepoBranchesRequester;
import sebastian.GHData.resource.RepoListRequester;
import sebastian.GHData.response.external.RepoObject;
import sebastian.GHData.response.inner.UserNotFound;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GetAndMapData {

    RepoBranchesRequester repoBranchesRequester = new RepoBranchesRequester();

    public ResponseEntity<String> processStringToJson(String username) {
        try {

            // request all user's repositories from GitHub API
            Map<String, Integer> jsonReposResponse = RepoListRequester.sendGetRequest(username);
            String body = jsonReposResponse.keySet().iterator().next();
            Integer satusCode = jsonReposResponse.get(body);

            assert body != null;
            if (satusCode.equals(404) || satusCode.equals(403)) {
                return handleBasicErrors(body, satusCode);
            }

            ObjectMapper mapper = new ObjectMapper();

            List<RepoObject> modifiedRepos = mapper.readValue(body, new TypeReference<List<RepoObject>>(){})
                    .stream()
                    .parallel()
                    .filter(repo -> repo.getFork().equals("false"))
                    .peek(repo -> repo.setFork(null))
                    .map(repo -> {
                        Map<String, Integer> repoBranchResponse = repoBranchesRequester.sendGetRequest(repo.getOwnerLogin(), repo.getRepositoryName());
                        String bodyBranch = repoBranchResponse.keySet().iterator().next();
                        Integer satusCodeBranch = repoBranchResponse.get(bodyBranch);
                        assert bodyBranch != null;
                        if (satusCodeBranch.equals(404) || satusCodeBranch.equals(403)) {
                            return repo;
                        }

                        ObjectMapper mapperBranch = new ObjectMapper();
                        try {
                            JsonNode jsonBranches = mapperBranch.readTree(bodyBranch);
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

    private ResponseEntity<String> handleBasicErrors(String body, Integer satusCode) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String notFoundResponse = mapper.writeValueAsString(new UserNotFound(satusCode, body));

        ResponseEntity<String> responseEntity = ResponseEntity.status(satusCode)
                .header("Content-Type", "application/json")
                .body(notFoundResponse);

        return responseEntity;
    }

    //public ResponseEntity<String> userNotFound(String json) throws JsonProcessingException {
    //    ObjectMapper mapper = new ObjectMapper();
    //    String notFoundResponse = mapper.writeValueAsString(new UserNotFound(404, json));
//
    //    ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND)
    //            .header("Content-Type", "application/json")
    //            .body(notFoundResponse);
//
    //    return responseEntity;
    //}
//
    //public ResponseEntity<String> requestsLimit(String json) throws JsonProcessingException {
    //    ObjectMapper mapper = new ObjectMapper();
    //    String requestsLimitResponse = mapper.writeValueAsString(new UserNotFound(403, json));
//
    //    ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
    //            .header("Content-Type", "application/json")
    //            .body(requestsLimitResponse);
//
    //    return responseEntity;
    //}

}
