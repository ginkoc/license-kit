package com.ginko.license.checker.predicates;

/**
 * @author ginko
 * @date 8/28/19
 */
public enum PredicateError {
    /***/
    NONE(0, "No error occur."),

    EARLY(1, "Date before validity period."),

    EXPIRED(2, "Date after validity period."),

    MAC_ERROR(3, "Mac doesn't match."),

    IP_ERROR(4, "Ip doesn't match."),
    ;

    private final int code;
    private final String message;

    PredicateError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
