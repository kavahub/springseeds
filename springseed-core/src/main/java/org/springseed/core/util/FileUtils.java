package org.springseed.core.util;

import java.util.Optional;

import lombok.experimental.UtilityClass;

/**
 * TODO
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@UtilityClass
public class FileUtils {
    public String getFileExtension(String filename) {
        return Optional.ofNullable(filename)
                       .filter(f -> f.contains("."))
                      .map(f -> f.substring(filename.lastIndexOf(".") + 1)).orElse(null);
    }
}
