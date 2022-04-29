package org.springseed.oss.minio.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * TODO
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class OSSMinioInternalException extends RuntimeException {

    public OSSMinioInternalException() {
    }

    public OSSMinioInternalException(String message) {
        super(message);
    }

    public OSSMinioInternalException(Throwable cause) {
        super(cause);
    }

    public OSSMinioInternalException(String message, Throwable cause) {
        super(message, cause);
    }

    public OSSMinioInternalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
