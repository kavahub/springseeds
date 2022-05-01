package org.springseed.oss.minio.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
public class RemoveServiceControllerLiveTests {
    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(username = "test", roles = { "oss_read", "oss_write", "oss_delete" })
    public void givenFile_whenRemove_thenOK() throws Exception {
        final String objectId = Const.uploadFile(mvc);

        mvc.perform(get("/v1/getservice/objects/data/{bucket}/{objectId}", Const.TEST_BUCKET, objectId))
                .andDo(print())
                .andExpect(status().isOk());

        mvc.perform(delete("/v1/removeservice/objects/{bucket}/{objectId}", Const.TEST_BUCKET, objectId))
                .andDo(print())
                .andExpect(status().isNoContent());

        mvc.perform(get("/v1/getservice/objects/data/{bucket}/{objectId}", Const.TEST_BUCKET, objectId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "test", roles = { "oss_read", "oss_write" })
    public void givenNoRoles_whenRemove_thenForbidden() throws Exception {
        mvc.perform(delete("/v1/removeservice/objects/{bucket}/{objectId}", Const.TEST_BUCKET, "objectId"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}
