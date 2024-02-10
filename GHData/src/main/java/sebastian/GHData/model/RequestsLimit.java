package sebastian.GHData.model;

import lombok.Data;

@Data
public class RequestsLimit {
    // Requests limit reached response
    private int status;
    private String message;

    public RequestsLimit(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
