package com.ginko.license.checker.predicates;

/**
 * @author ginko
 * @date 8/28/19
 */
public enum PredicateError {
    /**未出现异常*/
    NONE(0, "No error occur."),

    /**证书未到可使用的日期*/
    EARLY(1, "Date before validity period."),

    /**证书已经过期*/
    EXPIRED(2, "Date after validity period."),

    /**mac地址校验异常*/
    MAC_ERROR(3, "Mac doesn't match."),

    /**ip地址校验异常*/
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
