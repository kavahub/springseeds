package org.springseed.oss.local.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 文件资源异常
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class FileNotFoundException extends OSSLocalRuntimeException {

    public FileNotFoundException(String message) {
        super(message);
    }
}
