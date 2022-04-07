package org.springseed.oauth2.util;

/**
 * 抽象业务异常
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
public abstract class CommonRuntimeException extends RuntimeException {

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
