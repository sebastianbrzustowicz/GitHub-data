package sebastian.GHData.resource;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class repoBranchesRequester {
    // Request to GitHub API for repository branches

    public static String sendGetRequest(String username, String repoName) {

        try {
            String apiUrl = "https://api.github.com/repos/" + username + "/" + repoName + "/branches";

            HttpClient httpClient = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();

        } catch (Exception e) {
            System.err.println("Error while sending request: " + e.getMessage());
        }

        return null;
    }
}