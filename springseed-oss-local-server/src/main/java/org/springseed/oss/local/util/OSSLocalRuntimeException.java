package org.springseed.oss.local.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * OSS基础异常
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class OSSLocalRuntimeException extends RuntimeException {

    public OSSLocalRuntimeException() {
    }

    public OSSLocalRuntimeException(String message) {
        super(message);
    }

    public OSSLocalRuntimeException(Throwable cause) {
        super(cause);
    }

    public OSSLocalRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public OSSLocalRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
