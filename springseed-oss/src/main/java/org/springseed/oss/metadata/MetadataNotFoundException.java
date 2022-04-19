package org.springseed.oss.metadata;

import org.springseed.oss.util.OSSRuntimeException;

/**
 * 元数据未找到
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
public class MetadataNotFoundException extends OSSRuntimeException {

    public MetadataNotFoundException(String message) {
        super(message);
    }
    
}
