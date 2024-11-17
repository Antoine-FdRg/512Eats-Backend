package ssdbrestframework;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SSDBResponse<T> {
    private int statusCode;
    private T body;
    public static final int OK = 200;
    public static final int BAD_REQUEST = 400;
    public static final int METHOD_NOT_ALLOWED = 405;
    public static final int INTERNAL_SERVER_ERROR = 500;


    public SSDBResponse(int statusCode, T body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public static <T> SSDBResponse<T> ok(T body) {
        return new SSDBResponse<>(OK, body);
    }

    public static <T> SSDBResponse<T> status(int statusCode, T body) {
        return new SSDBResponse<>(statusCode, body);
    }
}