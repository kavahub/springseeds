package org.springseed.oss.metadata;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springseed.oss.ApplicationTestConfiguration;

/**
 * 测试
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ApplicationTestConfiguration.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MetadataControllerTests {
    @Autowired
    private MetadataRepository metadataRepository;

    @Autowired
    private MockMvc mvc;

    private Metadata md1 = Metadata.builder().path("path1").build();
    private Metadata md2 = Metadata.builder().path("path2").build();

    @BeforeAll

    public void BeforeAll() {
        metadataRepository.saveAllAndFlush(Arrays.asList(md1, md2));
    }

    @Test
    public void givenWrongId_whenGet_thenNotFound() throws Exception {
        mvc.perform(get("/v1/oss/metadatas/{id}", "wrong_id"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenIds_whenGetIds_thenOK() throws Exception {
        List<String> ids = Arrays.asList(md1.getId(), md2.getId(), "wrong_id");
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.addAll("ids", ids);

        mvc.perform(get("/v1/oss/metadatas/ids").params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(equalTo(2))));
    }
}
