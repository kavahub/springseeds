package org.springseed.oss.util;

import java.io.InputStream;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.IoUtil;
import lombok.experimental.UtilityClass;

/**
 * 工具
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
@UtilityClass
public class OSSUtil {
    private final static int MASK = 255;

    public String getFileChecksumCRC32(final InputStream fileData) {
        return Long.toHexString(IoUtil.checksumCRC32(fileData));
    }

    public String getFileType(final InputStream fileData, final String fileName) {
        return FileTypeUtil.getType(fileData, fileName);
    }

    /**
     * 生成文件存放路径，根据文件名hash值创建路径
     * 
     * @param fileName
     * @return
     */
    public String[] getFilePath(final String fileName) {
        final int hash = fileName.hashCode();
        final int firstDir = hash & MASK;
        final int secondDir = (hash >> 8) & MASK;

        return new String[] { String.format("%02x", firstDir).toUpperCase(),
                String.format("%02x", secondDir).toUpperCase() };
    }

}
