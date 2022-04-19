package org.springseed.oss.util;

/**
 * OSS基础异常
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
public class OSSRuntimeException extends RuntimeException {

    public OSSRuntimeException() {
    }

    public OSSRuntimeException(String message) {
        super(message);
    }

    public OSSRuntimeException(Throwable cause) {
        super(cause);
    }

    public OSSRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public OSSRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
