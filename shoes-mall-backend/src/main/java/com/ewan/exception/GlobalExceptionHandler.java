package com.ewan.exception;

import com.ewan.common.BaseResponse;
import com.ewan.common.ErrorCode;
import com.ewan.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.ArrayList;
import java.util.List;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("BusinessException", e);
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统错误");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse<?> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
//        log.error("RuntimeException", e);
//        return ResultUtils.error(ErrorCode.PARAMS_ERROR, e.getMessage());
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        List<String> errors = new ArrayList<>();
        for (FieldError fieldError : fieldErrors) {
            errors.add(fieldError.getDefaultMessage());
        }
        // 返回自定义的错误消息
        return ResultUtils.error(ErrorCode.PARAMS_ERROR, errors.toString());
    }


    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public BaseResponse<?> handleMaxUploadSizeExceededException(
            MaxUploadSizeExceededException ex) {
        log.error("BusinessException", ex);
        return ResultUtils.error(ErrorCode.PARAMS_ERROR, ex.toString());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public BaseResponse<?> illegalArgumentExceptionHandler(RuntimeException e) {
        log.error("RuntimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage());
    }
}
