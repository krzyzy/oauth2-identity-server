package com.solidify.oauth2.web;

public class ResponseMessage {

    private final ResponseStatus status;
    private final String message;

    protected ResponseMessage(ResponseStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public static ResponseMessage createOk(String message) {
        return new ResponseMessage(ResponseStatus.OK, message);
    }
    public static ResponseMessage createError(String message) {
        return new ResponseMessage(ResponseStatus.ERROR, message);
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
