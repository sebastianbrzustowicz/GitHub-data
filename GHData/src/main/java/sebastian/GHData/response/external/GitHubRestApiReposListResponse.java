package sebastian.GHData.response.external;

import lombok.Data;
import java.util.HashMap;

@Data
public class GitHubRestApiReposListResponse {
    // Repository variables from GitHub API
    private String repositoryName;
    private String ownerLogin;
    private HashMap<String, String> branchData;
    private int size;
    private String language;
}
