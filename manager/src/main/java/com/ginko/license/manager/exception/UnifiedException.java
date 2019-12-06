package com.ginko.license.manager.exception;

/**
 * @author ginko
 * @date 8/20/19
 */
public class UnifiedException extends RuntimeException {

    private static final long serialVersionUID = -6033485270335626843L;
    private int code;
    private String msg;

    public UnifiedException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
