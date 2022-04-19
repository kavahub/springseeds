package org.springseed.oss.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import cn.hutool.core.util.RandomUtil;

/**
 * TODO
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
public class OSSUtilTests {

    @Test
    public void givenFile_whenGetFileType_thenOk() throws IOException {
        final Resource file1 = new ClassPathResource("test-file.txt");
        assertThat(OSSUtil.getFileType(file1.getInputStream(), file1.getFilename())).isEqualTo("txt");

        final Resource file2 = new ClassPathResource("test-file2");
        assertThat(OSSUtil.getFileType(file2.getInputStream(), file2.getFilename())).isEqualTo("jpg");
    }

    @Test
    public void givenFileName_whenGetFilePath_thenOk() {
        assertThat(OSSUtil.getFilePath("我的文件.txt")).contains("A4", "62");
        assertThat(OSSUtil.getFilePath("我的文件")).contains("22", "98");
    }

    @Test
    public void givenOneMillionFileName_whenGetFilePath_thenCount() {
        final Random random = new Random();
        final int OneMillion = 1_000_000;

        final Map<String, Integer> count = new HashMap<>();
        for(int i = 0; i < OneMillion; i++) {
            final String fileName = RandomUtil.randomString(random.nextInt(100));
            final String key = String.join(",", OSSUtil.getFilePath(fileName));
            Integer value = count.get(key);
            if (value == null) {
                value = 0;
            }

            count.put(key, value + 1);
        }

        System.out.println("目录数：" + count.size());
        System.out.println("文件数前1000：" + count.values().stream().sorted(Comparator.reverseOrder()).limit(1000).collect(Collectors.toList()));
    }
}
