package sebastian.GHData.response.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.util.HashMap;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RepoObject {

    private String repositoryName;
    private String ownerLogin;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String fork;
    private String language;
    private int size;
    private HashMap<String, String> branches;

    @JsonProperty("name")
    public void setName(String name) {
        this.repositoryName = name;
    }

    @JsonProperty("owner")
    public void setOwner(JsonNode ownerNode) {
        this.ownerLogin = ownerNode.get("login").asText();
    }

    @JsonProperty("fork")
    public void setFork(String fork) {
        this.fork = fork;
    }

    @JsonProperty("language")
    public void setLanguage(String language) {
        this.language = language;
    }

    @JsonProperty("size")
    public void setName(Integer size) {
        this.size = size;
    }

    public void setBranchesMap(HashMap<String, String> branches) {
        this.branches = branches;
    }
}
