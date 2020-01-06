package com.ginko.license.checker.exception;

import com.ginko.license.checker.predicates.PredicateError;

/**
 * 对校验异常进行统一，方便使用者处理
 * @author ginko
 * @date 8/28/19
 */
public class CheckException extends Exception {

    private static final long serialVersionUID = -1757246051733317995L;
    private PredicateError error;

    public CheckException(String message, PredicateError error) {
        super(message);
        this.error = error;
    }

    public PredicateError getError() {
        return error;
    }
}
