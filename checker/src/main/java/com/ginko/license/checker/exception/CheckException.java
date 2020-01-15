package com.ginko.license.checker.exception;

/**
 * 对校验异常进行统一，方便使用者处理
 *
 * @author ginko
 * @date 8/28/19
 */
public class CheckException extends Exception {

    private static final long serialVersionUID = -1757246051733317995L;

    public CheckException(String message) {
        super(message);
    }
}
