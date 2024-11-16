package ssdbrestframework;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SSDBServer<T> {
    private int statusCode;
    private T body;

    public SSDBServer(int statusCode, T body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public static <T> SSDBServer<T> ok(T body) {
        return new SSDBServer<>(200, body);
    }

    public static <T> SSDBServer<T> status(int statusCode, T body) {
        return new SSDBServer<>(statusCode, body);
    }
}