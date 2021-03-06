package org.springseed.core.util;

import lombok.Data;

/**
 * 
 * 接口请求错误响应
 *
 * @author PinWei Wan
 * @since 1.0.0
 */
@Data
public class ErrorResponse {
    private String message;
    private String error;
    private String trace;

    public ErrorResponse(final String message) {
        super();
        this.message = message;
    }

    public ErrorResponse(final String message, final String error) {
        this(message);
        this.error = error;
    }

    public ErrorResponse(String message, String error, String trace) {
        this(message, error);
        this.trace = trace;
    }

    
}
