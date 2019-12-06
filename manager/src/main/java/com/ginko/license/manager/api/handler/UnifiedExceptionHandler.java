package com.ginko.license.manager.api.handler;

import com.ginko.license.manager.api.restentity.ResultEntity;
import com.ginko.license.manager.api.restentity.ResultUtil;
import com.ginko.license.manager.exception.UnifiedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author ginko
 * @date 8/20/19
 */
@RestControllerAdvice
public class UnifiedExceptionHandler {

    @ExceptionHandler(UnifiedException.class)
    public ResultEntity<Void> validationErrorHandler(UnifiedException ex) {
        return ResultUtil.error(ex.getCode(), ex.getMsg());
    }
}
