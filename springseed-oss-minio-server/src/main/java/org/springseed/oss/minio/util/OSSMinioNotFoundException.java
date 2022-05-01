package org.springseed.oss.minio.util;

/**
 * 异常
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
public class OSSMinioNotFoundException extends OSSMinioInternalException {

    public OSSMinioNotFoundException(String message) {
        super(message);
    }  
}
