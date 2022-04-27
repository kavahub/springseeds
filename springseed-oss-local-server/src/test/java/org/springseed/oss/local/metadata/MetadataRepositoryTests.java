package org.springseed.oss.local.metadata;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springseed.oss.SpringseedActiveProfiles;

/**
 * 元数据数据接口测试
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
@SpringseedActiveProfiles
public class MetadataRepositoryTests {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MetadataRepository metadataRepository;

    @Test
    public void giveMetadata_whenFind_thenOk() {
        final Metadata metadata = Metadata.builder()
                .createdBy("createdBy")
                .name("name")
                .path("path")
                .type("type")
                .size(1)
                .build();

        entityManager.persistAndFlush(metadata);

        final Metadata found = metadataRepository.findById(metadata.getId()).get();
        assertThat(found.getCreatedOn()).isNotNull();
        assertThat(found.getCreatedBy()).isEqualTo("createdBy");
        assertThat(found.getName()).isEqualTo("name");
        assertThat(found.getPath()).isEqualTo("path");
        assertThat(found.getType()).isEqualTo("type");
        assertThat(found.getSize()).isEqualTo(1);
    }
}
