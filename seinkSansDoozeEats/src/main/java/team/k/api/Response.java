package team.k.api;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Response<T> {
    private int statusCode;
    private T body;

    public Response(int statusCode, T body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public static <T> Response<T> ok(T body) {
        return new Response<>(200, body);
    }

    public static <T> Response<T> status(int statusCode, T body) {
        return new Response<>(statusCode, body);
    }
}