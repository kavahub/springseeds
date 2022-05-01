package org.springseed.oss;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springseed.core.util.SnowflakeUtil;

import lombok.experimental.UtilityClass;

/**
 * 常量
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@UtilityClass
public class Const {
    public final static String TEST_BUCKET = "docexamplebucket1";
    
    public MockMultipartFile createMockMultipartFile(final String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource(fileName);
        return new MockMultipartFile("file", resource.getFilename(), "multipart/form-data",
                resource.getInputStream());
    }
    public String uploadFile(MockMvc mvc) throws Exception, IOException {
        return uploadFile(mvc, "test-file.txt");
    }

    public String uploadFile(MockMvc mvc, String fileName) throws Exception, IOException {
        String objectId = SnowflakeUtil.INSTANCE.nextIdStr();
        mvc.perform(multipart("/v1/putservice/objects/{bucket}", Const.TEST_BUCKET)
                .file(Const.createMockMultipartFile(fileName))
                .param("objectId", objectId))
                .andDo(print())
                .andExpect(status().isCreated());
        return objectId;
    }
}
