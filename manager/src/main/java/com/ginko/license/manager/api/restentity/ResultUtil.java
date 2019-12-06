package com.ginko.license.manager.api.restentity;

/**
 * @author ginko
 * @date 8/20/19
 */
public class ResultUtil {

    public static ResultEntity<Void> success() {
        return success(null);
    }

    public static <T> ResultEntity<T> success(T data) {
        return new ResultEntity<>(0, "success", data);
    }

    public static ResultEntity<Void> error(int code, String msg) {
        return new ResultEntity<>(code, msg);
    }
}
