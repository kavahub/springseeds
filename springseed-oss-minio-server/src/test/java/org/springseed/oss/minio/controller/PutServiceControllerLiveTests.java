package org.springseed.oss.minio.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springseed.oss.Const;
import org.springseed.oss.OSSMinioApplication;
import org.springseed.oss.SpringseedActiveProfiles;

/**
 * 测试
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = OSSMinioApplication.class)
@SpringseedActiveProfiles
@AutoConfigureMockMvc
public class PutServiceControllerLiveTests {
    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(username = "write", roles = { "oss_write" })
    public void givenFile_whenPut_thenOK() throws Exception {
        final MvcResult result = mvc
                .perform(multipart("/v1/putservice/objects/{bucket}", Const.TEST_BUCKET)
                        .file(Const.createMockMultipartFile("test-file.txt")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isNotBlank();
    }

    @Test
    @WithMockUser(username = "read", roles = { "oss_read" })
    public void givenFile_whenPut_thenForbidden() throws Exception {
        mvc.perform(multipart("/v1/putservice/objects/{bucket}", Const.TEST_BUCKET)
                .file(Const.createMockMultipartFile("test-file.txt")))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}
