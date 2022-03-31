package org.springseed.oauth2.util;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

/**
 * 
 * 接口请求错误响应
 *
 * @author PinWei Wan
 * @since 1.0.0
 */
public class ErrorResponse {
    private String message;
    private String error;

    public ErrorResponse(final String message) {
        super();
        this.message = message;
    }

    public ErrorResponse(final String message, final String error) {
        super();
        this.message = message;
        this.error = error;
    }

    public ErrorResponse(List<ObjectError> allErrors, String error) {
        this.error = error;
        String temp = allErrors.stream().map(e -> {
            if (e instanceof FieldError) {
                return "{\"field\":\"" + ((FieldError) e).getField() + "\",\"defaultMessage\":\"" + e.getDefaultMessage() + "\"}";
            } else {
                return "{\"object\":\"" + e.getObjectName() + "\",\"defaultMessage\":\"" + e.getDefaultMessage() + "\"}";
            }
        }).collect(Collectors.joining(","));
        this.message = "[" + temp + "]";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(final String error) {
        this.error = error;
    }   
}
