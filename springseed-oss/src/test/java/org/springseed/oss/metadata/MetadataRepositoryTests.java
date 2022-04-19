package org.springseed.oss.metadata;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * TODO
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class MetadataRepositoryTests {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MetadataRepository metadataRepository;

    @Test
    public void giveMetadata_whenFind_thenOk() {
        final Metadata metadata = Metadata.builder().crc32("crc32")
                .creaateBy("creaateBy").name("name").path("path").size(1).build();

        entityManager.persistAndFlush(metadata);

        final Metadata found = metadataRepository.findById(metadata.getId()).get();
        assertThat(found.getCreatedOn()).isNotNull();
        assertThat(found.getPath()).isEqualTo("path");
        assertThat(found.getCreaateBy()).isEqualTo("creaateBy");
        assertThat(found.getCrc32()).isEqualTo("crc32");
        assertThat(found.getSize()).isEqualTo(1);
    }
}
