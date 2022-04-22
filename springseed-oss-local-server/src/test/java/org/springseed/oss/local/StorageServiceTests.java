package org.springseed.oss.local;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springseed.oss.LocalOSSApplication;
import org.springseed.oss.SpringseedActiveProfiles;
import org.springseed.oss.local.config.OSSProperties;
import org.springseed.oss.local.util.FileNotFoundException;
import org.springseed.oss.metadata.Metadata;
import org.springseed.oss.metadata.MetadataNotFoundException;
import org.springseed.oss.metadata.MetadataRepository;

/**
 * 测试
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = LocalOSSApplication.class)
@SpringseedActiveProfiles
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StorageServiceTests {
    @Autowired
    private StorageService storageService;
    @Autowired
    private MetadataRepository metadataRepository;
    @Autowired
    private OSSProperties properties;

    private Metadata md = Metadata.builder().name("name").path("path").build();

    @BeforeAll
    public void BeforeAll() {
        metadataRepository.save(md);
    }

    @Test
    public void thenNotNull() {
        assertThat(storageService).isNotNull();
    }

    @Test
    public void givenWrongId_whenLoadByMetadataId_thenMetadataNotFoundException() {
        assertThrows(MetadataNotFoundException.class, () -> storageService.loadByMetadataId("wrongId"));
    }

    @Test
    public void givenWrongId_whenLoadByMetadata_thenFileResourceException() {
        FileNotFoundException exception = assertThrows(FileNotFoundException.class, () -> storageService.loadByMetadata(md));
        System.out.println(exception.getMessage());
    }

    @Test
    public void givenFile_whenStore_thenOK() throws IOException {
        final String metadataId =  this.storeFile();
        final Metadata metadata = metadataRepository.findById(metadataId).get();

        final Path filePath = this.getFilePath(metadata);
        assertThat(Files.exists(filePath)).isTrue();
        assertThat(Files.readString(filePath)).isEqualTo("Hello");
    }

    @Test
    public void givenFile_whenStoreAndLoad_thenOK() {
        final String metadataId =  this.storeFile();
        final Resource resource = storageService.loadByMetadataId(metadataId);
        assertThat(resource.exists()).isTrue();
    }

    @Test
    public void givenFile_whenStoreAndRemove_thenOK() {
        final String metadataId = this.storeFile();

        final Metadata metadata = metadataRepository.findById(metadataId).get();
        storageService.removeByMetadataId(metadataId);
        assertThrows(MetadataNotFoundException.class, () -> storageService.loadByMetadataId(metadataId));
        assertThat(Files.exists(this.getFilePath(metadata))).isFalse();
    }

    private Path getFilePath(final Metadata metadata) {
        return properties.getUploadRootPath().resolve(metadata.getPath()).resolve(metadata.getId());
    }

    private String storeFile() {
        return storageService.store(new MockMultipartFile("foo", "foo.txt", MediaType.TEXT_PLAIN_VALUE,
        "Hello".getBytes()));
    }
}