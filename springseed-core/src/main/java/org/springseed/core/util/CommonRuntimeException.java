package org.springseed.core.util;

/**
 * 
 * 通用异常，所有业务异常需继承此类
 *
 * @author PinWei Wan
 * @since 1.0.0
 */
public class CommonRuntimeException extends RuntimeException{

    public CommonRuntimeException() {
    }

    public CommonRuntimeException(String message) {
        super(message);
    }

    public CommonRuntimeException(Throwable cause) {
        super(cause);
    }

    public CommonRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommonRuntimeException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
