package sebastian.GHData.resource;

import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class RepoBranchesRequester {
    // Request to GitHub API for repository branches

    @Autowired
    private final HttpClient httpClient = HttpClient.newHttpClient();


    public Map<String, Integer> sendGetRequest(String username, String repoName) {

        try {
            String apiUrl = "https://api.github.com/repos/" + username + "/" + repoName + "/branches";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return Map.of(response.body(), response.statusCode());

        } catch (Exception e) {
            System.err.println("Error while sending request: " + e.getMessage());
        }

        return null;
    }
}