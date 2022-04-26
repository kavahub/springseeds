package org.springseed.oss.local.util;

import java.io.File;
import java.util.Optional;

import lombok.experimental.UtilityClass;

/**
 * 工具
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
@UtilityClass
public class OSSUtils {
    private final static int MASK = 255;

    public String getFileType(final String fileName) {
        return Optional.ofNullable(fileName)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(fileName.lastIndexOf(".") + 1)).orElse(null);
    }

    /**
     * 生成文件存放路径，根据文件名hash值创建路径
     * 
     * @param fileName
     * @return
     */
    public String getFilePath(final String fileName) {
        final int hash = fileName.hashCode();
        final int firstDir = hash & MASK;
        final int secondDir = (hash >> 8) & MASK;

        final String path = new StringBuilder(String.format("%02x", firstDir))
                .append(File.separator)
                .append(String.format("%02x", secondDir))
                .toString();

        return path.toUpperCase();
    }

}
