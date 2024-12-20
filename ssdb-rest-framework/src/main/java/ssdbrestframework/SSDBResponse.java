package ssdbrestframework;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SSDBResponse {
    private String message;
    public static final int OK = 200;
    public static final int BAD_REQUEST = 400;
    public static final int METHOD_NOT_ALLOWED = 405;
    public static final int INTERNAL_SERVER_ERROR = 500;


    public SSDBResponse(String message) {
        this.message = message;
    }
}