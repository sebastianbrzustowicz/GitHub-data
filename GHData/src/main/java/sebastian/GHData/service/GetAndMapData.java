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
import sebastian.GHData.response.external.GitHubRestApiReposListResponse;
import sebastian.GHData.response.external.RepoObject;
import sebastian.GHData.response.inner.UserNotFound;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GetAndMapData {

    public ResponseEntity<String> processStringToJson(String username) {
        try {

            // request all user's repositories from GitHub API
            String jsonReposResponse = repoListRequester.sendGetRequest(username);
            ObjectMapper objectMapper = new ObjectMapper();

            if (jsonReposResponse.equals("User not found")) {
                String notFoundResponse = objectMapper.writeValueAsString(new UserNotFound(404, jsonReposResponse));

                ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Content-Type", "application/json")
                        .body(notFoundResponse);

                return responseEntity;
            } else if (jsonReposResponse.equals("The limit of requests for the GitHub API from this IP address has been reached")) {
                String requestsLimitResponse = objectMapper.writeValueAsString(new UserNotFound(403, jsonReposResponse));

                ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                        .header("Content-Type", "application/json")
                        .body(requestsLimitResponse);

                return responseEntity;
            }

            ObjectMapper mapper = new ObjectMapper();
            List<RepoObject> repoList = mapper.readValue(jsonReposResponse, new TypeReference<List<RepoObject>>(){});

            for (RepoObject obj : repoList) {
                System.out.println(obj.toString());
            }

            //List<RepoObject> repoList = new ArrayList<>();

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
