package sebastian.GHData.response.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RepoObject {
    private String repositoryName;
    private String ownerLogin;
    private String language;
    private int size;

    @JsonProperty("name")
    public void setName(String name) {
        this.repositoryName = name;
    }

    @JsonProperty("owner")
    public void setOwner(JsonNode ownerNode) {
        this.ownerLogin = ownerNode.get("login").asText();
    }

    @JsonProperty("language")
    public void setLanguage(String language) {
        this.language = language;
    }

    @JsonProperty("size")
    public void setName(Integer size) {
        this.size = size;
    }
}
