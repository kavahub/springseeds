package org.springseed.oss.local.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springseed.oss.util.OSSRuntimeException;

/**
 * 文件资源异常
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class FileReadException extends OSSRuntimeException {

    public FileReadException(Throwable cause) {
        super(cause);
    }

    public FileReadException(String message) {
        super(message);
    }


}
