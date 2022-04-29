package org.springseed.oss.minio.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * TODO
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class OSSMinioException extends OSSMinioInternalException {

    public OSSMinioException(String message) {
        super(message);
    }  
}
