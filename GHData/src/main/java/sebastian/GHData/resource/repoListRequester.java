package sebastian.GHData.resource;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class repoListRequester {
    // Request to GitHub API for list of repositories

    public static String sendGetRequest(String username) {

        try {
            String apiUrl = "https://api.github.com/users/" + username + "/repos";

            HttpClient httpClient = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 404) {
                return("User not found");
            }

            return response.body();

        } catch (Exception e) {
            System.err.println("Error while sending request: " + e.getMessage());
        }

        return null;
    }

}
