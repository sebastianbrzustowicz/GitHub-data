package sebastian.GHData.model;

import lombok.Data;

@Data
public class UserNotFound {
    // User not found response
    private int status;
    private String message;

    public UserNotFound(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
