package org.springseed.oss.local;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springseed.oss.SpringseedActiveProfiles;

/**
 * 测试
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@SpringseedActiveProfiles
@AutoConfigureMockMvc
public class OSSLocalControllerTests {
    @Autowired
    private MockMvc mvc;


    @Test
    @WithMockUser(username="test",roles={"oss_read", "oss_write", "oss_delete"})
    public void givenFile_whenUploadAndDownload_thenOK() throws Exception {
        // upload
        final MvcResult result1 = mvc.perform(multipart("/v1/oss-local/upload").file(this.createMockMultipartFile()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        final String metadataId = result1.getResponse().getContentAsString();   

        // load
        final MvcResult result2 = mvc.perform(get("/v1/oss-local/download/{metadataId}", metadataId))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        final String fileData = new String(result2.getResponse().getContentAsByteArray());
        assertThat(fileData).isEqualTo("你好");
    }

    @Test
    @WithMockUser(username="test",roles={"oss_read", "oss_delete"})
    public void givenWrongRole_whenUpload_thenForbidden() throws Exception {
        mvc.perform(multipart("/v1/oss-local/upload").file(this.createMockMultipartFile()))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username="test",roles={"oss_write", "oss_delete"})
    public void givenWrongRole_whenLoad_thenForbidden() throws Exception {
        mvc.perform(get("/v1/oss-local/download/id"))
        .andDo(print())
        .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username="test",roles={"oss_write", "oss_read"})
    public void givenWrongRole_whenRemove_thenForbidden() throws Exception {
        mvc.perform(delete("/v1/oss-local/id"))
        .andDo(print())
        .andExpect(status().isForbidden());
    }

    private MockMultipartFile createMockMultipartFile() throws IOException {
        ClassPathResource resource = new ClassPathResource("test-file.txt");
        return new MockMultipartFile("file", resource.getFilename(), "multipart/form-data",
                resource.getInputStream());
    }
}
