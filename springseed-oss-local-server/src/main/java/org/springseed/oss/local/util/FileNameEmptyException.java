package org.springseed.oss.local.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springseed.oss.util.OSSRuntimeException;

/**
 * TODO
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class FileNameEmptyException extends OSSRuntimeException {
    
}
