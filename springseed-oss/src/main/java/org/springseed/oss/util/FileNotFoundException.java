package org.springseed.oss.util;

/**
 * 文件找不到
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
public class FileNotFoundException extends OSSRuntimeException {

    public FileNotFoundException(String message) {
        super(message);
    }

    public FileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }


}
