package org.springseed.oss.local.metadata;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springseed.oss.OSSLocalApplication;
import org.springseed.oss.SpringseedActiveProfiles;

/**
 * 元数据查询服务测试
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = OSSLocalApplication.class)
@SpringseedActiveProfiles
public class MetadataSaveServiceTests {
    @Autowired
    private MetadataSaveService metadataSaveService;
    @Autowired
    private MetadataQueryService metadataQueryService;

    @Test
    public void giveNullName_whenSave_thenGetDefault() {
        final Metadata md = metadataSaveService.save(null, "path", 0);
        assertThat(md.getName()).isEqualTo(MetadataSaveService.UN_NAMED_FILE);
        assertThat(md.getType()).isNull();
    }


    @Test
    public void whenUpdateName_thenShouldBeOk() {
        final Metadata md = metadataSaveService.save("name", "path", 0);
        assertThat(md.getName()).isEqualTo("name");

        metadataSaveService.updateName(md.getId(), "new file name");
        assertThat(metadataQueryService.findById(md.getId()).get().getName()).isEqualTo("new file name");
    }
}
