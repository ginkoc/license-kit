package com.ginko.license.common.utils.cmd;

/**
 * @author ginko
 * @date 7/20/19
 */
public enum CommandReturnCode {

    /**indicate command execute successfully*/
    SUCCESS(0),

    /**indicate command execute failed*/
    FAIL(1);

    private final int code;

    CommandReturnCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return String.valueOf(code);
    }
}
