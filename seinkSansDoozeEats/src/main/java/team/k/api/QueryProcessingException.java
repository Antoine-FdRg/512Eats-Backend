package team.k.api;

import lombok.Getter;

@Getter
public class QueryProcessingException extends Exception {
    private final int statusCode;
    private String exception;
    public static final String MAL_FORMED_PARAMS = "Bad request, it is likely that the request parameters or body are not correctly formatted";
    public static final String INTERNAL_SERVER_ERROR = "Internal server error";
    public static final String METHOD_NOT_ALLOWED = "Method not allowed";


    public QueryProcessingException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public QueryProcessingException(int statusCode, String message, String exception) {
        super(message);
        this.statusCode = statusCode;
        this.exception = exception;
    }

    @Override
    public String toString() {
        return "QueryProcessingException{" +
                "statusCode=" + statusCode +
                ", message='" + getMessage() + '\'' +
                ", exception='" + exception + '\'' +
                '}';
    }
}
