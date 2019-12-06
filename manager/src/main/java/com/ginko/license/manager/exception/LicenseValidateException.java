package com.ginko.license.manager.exception;

/**
 * @author ginko
 * @date 8/3/19
 */
public class LicenseValidateException extends RuntimeException {

    private String msg;

    public LicenseValidateException(String msg) {
        super("Validate license field failed, reason:" + msg);
        this.msg = msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
