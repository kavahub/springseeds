package org.springseed.oss.local;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springseed.oss.OSSLocalApplication;
import org.springseed.oss.SpringseedActiveProfiles;
import org.springseed.oss.local.metadata.MetadataQueryService;

/**
 * 测试
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = OSSLocalApplication.class)
@SpringseedActiveProfiles
@AutoConfigureMockMvc
public class OSSLocalControllerTests {
        @Autowired
        private MetadataQueryService metadataQueryService;
        @Autowired
        private MockMvc mvc;

        @Test
        @WithMockUser(username = "test", roles = { "oss_read", "oss_write" })
        public void givenFile_whenUploadAndDownload_thenContentOK() throws Exception {
                // upload
                final MvcResult result1 = mvc
                                .perform(multipart("/v1/files/upload").file(this.createMockMultipartFile()))
                                .andDo(print())
                                .andExpect(status().isCreated())
                                .andReturn();

                final String metadataId = result1.getResponse().getContentAsString();

                // load
                final MvcResult result2 = mvc.perform(get("/v1/files/download/{metadataId}", metadataId))
                                .andDo(print())
                                .andExpect(status().isOk()).andReturn();

                // 文件内容测试
                final String fileData = new String(result2.getResponse().getContentAsByteArray());
                assertThat(fileData).isEqualTo("你好");

                // 创建人测试
                metadataQueryService.findById(metadataId).ifPresent(metadata -> assertThat(metadata.getCreatedBy()).isEqualTo("test"));
        }

        @Test
        @WithMockUser(roles = { "oss_read", "oss_write", "oss_delete" })
        public void givenFile_whenUploadAndRemove_thenOK() throws Exception {
                // upload
                final MvcResult result1 = mvc
                                .perform(multipart("/v1/files/upload").file(this.createMockMultipartFile()))
                                .andDo(print())
                                .andExpect(status().isCreated())
                                .andReturn();

                final String metadataId = result1.getResponse().getContentAsString();
                // load
                mvc.perform(get("/v1/files/download/{metadataId}", metadataId))
                                .andDo(print())
                                .andExpect(status().isOk());

                // remove
                mvc.perform(delete("/v1/files/{metadataId}", metadataId))
                                .andDo(print())
                                .andExpect(status().isNoContent());

                // load
                mvc.perform(get("/v1/files/download/{metadataId}", metadataId))
                                .andDo(print())
                                .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser(roles = { "oss_read", "oss_write" })
        public void givenFile_whenAllInZip_thenOK() throws Exception {
                // upload
                final MvcResult result1 = mvc
                                .perform(multipart("/v1/files/upload").file(this.createMockMultipartFile()))
                                .andDo(print())
                                .andExpect(status().isCreated())
                                .andReturn();
                final String metadataId1 = result1.getResponse().getContentAsString();
                final MvcResult result2 = mvc
                                .perform(multipart("/v1/files/upload").file(this.createMockMultipartFile()))
                                .andDo(print())
                                .andExpect(status().isCreated())
                                .andReturn();
                final String metadataId2 = result2.getResponse().getContentAsString();

                // 下载zip
                MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
                params.addAll("objectIds", Arrays.asList(metadataId1, metadataId2, "wrong_id"));
                final MvcResult result3 = mvc.perform(get("/v1/files/download/all-in-zip").params(params))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andReturn();

                // 保存到target
                final Path zipFile = Paths.get("target", "all-in-zip.zip");
                final byte[] zipData = result3.getResponse().getContentAsByteArray();
                Files.copy(new ByteArrayInputStream(zipData), zipFile, StandardCopyOption.REPLACE_EXISTING);
        }

        @Test
        @WithMockUser(username = "test", roles = { "oss_write" })
        public void givenUploadFile_whenGetCreatedBy_thenOK() throws Exception {
                // upload
                final MvcResult result1 = mvc
                                .perform(multipart("/v1/files/upload").file(this.createMockMultipartFile()))
                                .andDo(print())
                                .andExpect(status().isCreated())
                                .andReturn();

                final String metadataId = result1.getResponse().getContentAsString();

                // 创建人测试
                metadataQueryService.findById(metadataId).ifPresent(metadata -> assertThat(metadata.getCreatedBy()).isEqualTo("test"));
        }


        @Test
        @WithMockUser(roles = { "oss_read", "oss_delete" })
        public void givenWrongRole_whenUpload_thenForbidden() throws Exception {
                mvc.perform(multipart("/v1/files/upload").file(this.createMockMultipartFile()))
                                .andDo(print())
                                .andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(roles = { "oss_write", "oss_delete" })
        public void givenWrongRole_whenLoad_thenForbidden() throws Exception {
                mvc.perform(get("/v1/files/download/id"))
                                .andDo(print())
                                .andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(roles = { "oss_write", "oss_read" })
        public void givenWrongRole_whenRemove_thenForbidden() throws Exception {
                mvc.perform(delete("/v1/files/id"))
                                .andDo(print())
                                .andExpect(status().isForbidden());
        }

        private MockMultipartFile createMockMultipartFile() throws IOException {
                ClassPathResource resource = new ClassPathResource("test-file.txt");
                return new MockMultipartFile("file", resource.getFilename(), "multipart/form-data",
                                resource.getInputStream());
        }
}
