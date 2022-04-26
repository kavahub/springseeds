package org.springseed.oss.local.metadata;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springseed.oss.local.util.LocalOSSRuntimeException;

/**
 * 元数据未找到
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class MetadataNotFoundException extends LocalOSSRuntimeException {

    public MetadataNotFoundException(String message) {
        super(message);
    }
    
}
