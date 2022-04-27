package org.springseed.core.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * TODO
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
public class FileUtilsTests {
    @Test
    public void givenFile_whenGetFileExtension_thenOk() {
        assertThat(FileUtils.getFileExtension("test-file.txt")).isEqualTo("txt");

        assertThat(FileUtils.getFileExtension("test-file")).isNull();
    }
}
