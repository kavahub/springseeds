package org.springseed.oss.local.util;

import static org.springframework.util.StringUtils.hasText;

import java.util.HashMap;
import java.util.Map;

import cn.hutool.core.text.StrFormatter;

/**
 * TODO
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
public class FileNameCounter {
    public final static String UNKNOW_FILE_NAME = "未知文件名";
    private Map<String, Integer> nameCache = new HashMap<>();

    public String count(final String fileName) {
        String tmpFileName = fileName;
        if (!hasText(tmpFileName)) {
            tmpFileName = UNKNOW_FILE_NAME;
        }

        Integer value = nameCache.get(tmpFileName);
        if (value == null) {
            value = -1;
        }

        value += 1;
        nameCache.put(tmpFileName, value);

        if (value.intValue() > 0) {
            // 修改文件名称，增加计数到文件名
            final int index = tmpFileName.indexOf(".");
            if (index < 0) {
                return tmpFileName + " - " + value;
            }

            return StrFormatter.format("{} - {}{}",
                    tmpFileName.substring(0, index),
                    value,
                    tmpFileName.substring(index));
        }
        return tmpFileName;
    }
}
