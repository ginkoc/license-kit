package com.ginko.license.manager.api.handler;

import com.ginko.license.manager.api.restentity.ResultEntity;
import com.ginko.license.manager.api.restentity.ResultUtil;
import com.ginko.license.manager.exception.LicenseValidateException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ginko
 * @date 8/20/19
 */
@RestControllerAdvice
public class ValidateExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultEntity<Void> validationErrorHandler(MethodArgumentNotValidException ex) {

        List<String> errorInformation = ex.getBindingResult().getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());
        return ResultUtil.error(-1, errorInformation.toString());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResultEntity<Void> validationErrorHandler(ConstraintViolationException ex) {
        List<String> errorInformation = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        return ResultUtil.error(-1, errorInformation.toString());
    }

    @ExceptionHandler(LicenseValidateException.class)
    public ResultEntity<Void> validationErrorHandler(LicenseValidateException ex) {
        return ResultUtil.error(-1, ex.getMsg());
    }
}
