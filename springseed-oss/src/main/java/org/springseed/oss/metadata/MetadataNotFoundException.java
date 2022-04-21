package org.springseed.oss.metadata;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springseed.oss.util.OSSRuntimeException;

/**
 * 元数据未找到
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class MetadataNotFoundException extends OSSRuntimeException {

    public MetadataNotFoundException(String message) {
        super(message);
    }
    
}
