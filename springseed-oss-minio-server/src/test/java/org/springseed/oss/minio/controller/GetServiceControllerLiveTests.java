package org.springseed.oss.minio.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springseed.oss.Const;
import org.springseed.oss.OSSMinioApplication;
import org.springseed.oss.SpringseedActiveProfiles;

import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public class GetServiceControllerLiveTests {
    @Autowired
    private MockMvc mvc;


    @Test
    @WithMockUser(username = "test", roles = { "oss_read", "oss_write" })
    public void givenObjectId_whenGetObject_thenContentOK() throws Exception {
        final String objectId = Const.uploadFile(mvc);

        final MvcResult result = mvc
                .perform(get("/v1/getservice/objects/data/{bucket}/{objectId}", Const.TEST_BUCKET, objectId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        // 文件内容测试
        final String fileData = new String(result.getResponse().getContentAsByteArray());
        assertThat(fileData).isEqualTo("你好");
    }

    @Test
    @WithMockUser(username = "test", roles = { "oss_read", "oss_write" })
    public void givenWrongObjectId_whenGetObject_thenBadRequest() throws Exception {
        mvc.perform(get("/v1/getservice/objects/data/{bucket}/{objectId}", Const.TEST_BUCKET, "wrongobjectid"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "test", roles = { "oss_read", "oss_write" })
    public void givenObjectIds_whenGetObjects_thenOk() throws Exception {
        final String objectId = Const.uploadFile(mvc);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.addAll("objectIds", Arrays.asList(objectId));

        final MvcResult result = mvc.perform(get("/v1/getservice/objects/data-zip/{bucket}", Const.TEST_BUCKET).params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        // 保存到target
        final Path zipFile = Paths.get("target", "data-zip.zip");
        final byte[] zipData = result.getResponse().getContentAsByteArray();
        Files.copy(new ByteArrayInputStream(zipData), zipFile, StandardCopyOption.REPLACE_EXISTING);

        // 压缩包内容
        try (final ZipFile zip = new ZipFile(zipFile.toFile())) {
            Enumeration<? extends ZipEntry> entries = zip.entries();
            assertThat(entries.nextElement().getName()).isEqualTo("test-file.txt");
        }
    }

    @Test
    @WithMockUser(username = "test", roles = { "oss_read", "oss_write" })
    public void givenObjectIds_whenGetPresignedObjectUrl_thenOk() throws Exception {
        final String objectId = Const.uploadFile(mvc);

        final String result = mvc.perform(get("/v1/getservice/objects/presigned-url/{bucket}/{objectId}/GET", Const.TEST_BUCKET, objectId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        log.info(">>>GetPresignedObjectUrl: {}", result);
        final URL url = new URL(result);
        assertThat(url.getHost()).isNotBlank();
        assertThat(url.getQuery()).isNotBlank();
    }

    @Test
    @WithMockUser(username = "test")
    public void givenNoRoles_whenGetObject_thenForbidden() throws Exception {
        mvc.perform(get("/v1/getservice/objects/data/{bucket}/{objectId}", Const.TEST_BUCKET, "wrongobjectid"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }


}
