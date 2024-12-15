package com.rubal.lld.httpserver.model;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public enum HttpStatus {
    SUCCESS(200, "OK"),
    ACCEPTED(201, "Accepted"),
    UNAUTHORIZED(401, "Unauthorized"),
    BAD_REQUEST(400, "Bad Request"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    private final int code;
    private final String status;

    HttpStatus(int code, String status) {
        this.code=code;
        this.status=status;
    }
}
