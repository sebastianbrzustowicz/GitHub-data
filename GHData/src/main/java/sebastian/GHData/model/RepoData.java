package sebastian.GHData.model;

import lombok.Data;
import java.util.HashMap;

@Data
public class RepoData {
    // Repository variables from GitHub API
    private String repositoryName;
    private String ownerLogin;
    private HashMap<String, String> branchData;
}
