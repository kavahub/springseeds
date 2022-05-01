package org.springseed.oss.minio.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springseed.oss.Const;
import org.springseed.oss.OSSMinioApplication;
import org.springseed.oss.SpringseedActiveProfiles;
import org.springseed.oss.minio.util.MinioUtils;

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
public class StatServiceControllerLiveTests {
    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(username = "test", roles = { "oss_read", "oss_write" })
    public void givenFile_whenGetStat_thenOK() throws Exception {
        final String objectId = Const.uploadFile(mvc);

        mvc.perform(get("/v1/statservice/objects/stat/{bucket}/{objectId}", Const.TEST_BUCKET, objectId)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.etag").isString())
                .andExpect(jsonPath("$.size").isNumber())
                .andExpect(jsonPath("$.lastModified").isNotEmpty());
    }

    @Test
    @WithMockUser(username = "test", roles = { "oss_read", "oss_write" })
    public void givenFile_whenGetUsermetadata_thenOK() throws Exception {
        final String objectId = Const.uploadFile(mvc);

        mvc.perform(get("/v1/statservice/objects/usermetadata/{bucket}/{objectId}", Const.TEST_BUCKET, objectId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.objectid").value(objectId))
                .andExpect(jsonPath("$.bucket").value(Const.TEST_BUCKET))
                .andExpect(jsonPath("$.minioid").value(MinioUtils.minioId(objectId)))
                .andExpect(jsonPath("$.filename").value("test-file.txt"))
                .andExpect(jsonPath("$.filesize").isNotEmpty())
                .andExpect(jsonPath("$.createdon").isNotEmpty())
                .andExpect(jsonPath("$.contenttype").value("text/plain"))
                .andExpect(jsonPath("$.createdby").value("test"));
    }

    @Test
    @WithMockUser(username = "test")
    public void givenNoRoles_whenGetUsermetadata_thenForbidden() throws Exception {
        mvc.perform(get("/v1/statservice/objects/usermetadata/{bucket}/{objectId}", Const.TEST_BUCKET, "objectId"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}
