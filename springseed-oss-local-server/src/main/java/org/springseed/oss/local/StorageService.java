package org.springseed.oss.local;

import static org.springframework.util.StringUtils.hasText;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springseed.core.util.PathUtils;
import org.springseed.oss.local.config.OSSProperties;
import org.springseed.oss.local.metadata.Metadata;
import org.springseed.oss.local.metadata.MetadataQueryService;
import org.springseed.oss.local.metadata.MetadataRepository;
import org.springseed.oss.local.metadata.MetadataSaveService;
import org.springseed.oss.local.util.FileNotFoundException;
import org.springseed.oss.local.util.OSSLocalRuntimeException;

import lombok.extern.slf4j.Slf4j;

/**
 * 文件系统存储服务
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
@Service
@Slf4j
public class StorageService {
    @Autowired
    private OSSProperties ossProperties;
    @Autowired
    private MetadataRepository metadataRepository;
    @Autowired
    private MetadataQueryService metadataQueryService;
    @Autowired
    private MetadataSaveService metadataSaveService;

    private Path uploadRootPath;

    @PostConstruct
    public void init() {
        this.uploadRootPath = ossProperties.getUploadRootPath();

        if (!Files.exists(uploadRootPath)) {
            try {
                Files.createDirectories(uploadRootPath);
            } catch (IOException e) {
                log.error("Unable to create directory", e);
            }
        }
    }

    /**
     * 存储文件
     * 
     * @param file
     * @return 元数据ID
     */
    @Transactional
    public String store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new OSSLocalRuntimeException("不能是空文件");
            }

            if (log.isDebugEnabled()) {
                log.debug("Begin to store file: {}", file.getOriginalFilename());
            }

            final String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            if (!hasText(fileName)) {
                throw new OSSLocalRuntimeException("文件名是必须的");
            }

            try (InputStream fileData = file.getInputStream()) {

                final long fileSize = file.getSize();
                final String[] filePath = PathUtils.generalHashPath(fileName);

                final Path destinationPath = this.uploadRootPath.resolve(filePath[0]).resolve(filePath[1]);
                if (!Files.exists(destinationPath)) {
                    Files.createDirectories(destinationPath);
                }

                // 存储文件元数据
                final Metadata metadata = metadataSaveService.save(fileName, Metadata.joinPath(filePath), fileSize);
                // 存储文件
                final long size = Files.copy(fileData, destinationPath.resolve(metadata.getId()),
                        StandardCopyOption.REPLACE_EXISTING);
                if (log.isDebugEnabled()) {
                    log.debug("Store file completed, object id: {}, file path: {}, file size: {}", metadata.getId(),
                            metadata.getFullPath(), size);
                }
                return metadata.getId();
            }

        } catch (IOException e) {
            throw new OSSLocalRuntimeException("Failed to store file", e);
        }
    }

    /**
     * 读取文件
     * 
     * @param fileId 元数据ID
     * @return
     */
    public Resource loadByObjectId(final String objectId) {
        return this.metadataQueryService.findById(objectId)
                .map(this::loadByMetadata)
                .get();
    }

    /**
     * 批量读取文件
     * 
     * @param objectIds 元数据ID列表
     * @return
     */
    public List<Resource> loadByObjectIds(final List<String> objectIds) {
        final List<Metadata> metadatas = this.metadataRepository.findAllById(objectIds);
        return this.loadByMetadatas(metadatas);
    }

    /**
     * 批量读取文件
     * 
     * @param metadatas 元数据列表
     * @return
     */
    public List<Resource> loadByMetadatas(final List<Metadata> metadatas) {
        return metadatas
                .stream()
                .map(metadata -> {
                    try {
                        return this.loadByMetadata(metadata);
                    } catch (FileNotFoundException ex) {
                        log.error(ex.getMessage());
                    }

                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 读取文件
     * 
     * @param metadata 元数据
     * @return
     */
    public Resource loadByMetadata(Metadata metadata) {
        if (log.isDebugEnabled()) {
            log.debug("Begin to load file: {}", metadata.getId());
        }

        final Path fileFullPath = this.uploadRootPath.resolve(metadata.getFullPath()).resolve(metadata.getId());
        try {
            final Resource resource = new UrlResource(fileFullPath.toUri());

            if (!resource.exists()) {
                throw new FileNotFoundException(fileFullPath.toString());
            }

            if (!resource.isReadable()) {
                throw new OSSLocalRuntimeException("文件不可读");
            }

            return resource;
        } catch (MalformedURLException e) {
            throw new OSSLocalRuntimeException("文件读取失败");
        }
    }

    /**
     * 删除文件
     * 
     * @param id 元数据ID
     */
    @Transactional
    public void removeByObjectId(final String objectId) {
        this.metadataRepository.findById(objectId).ifPresentOrElse(metadata -> {
            if (log.isDebugEnabled()) {
                log.debug("Begin to remove file: {}", metadata.getName());
            }

            final Path fileFullPath = this.uploadRootPath.resolve(metadata.getFullPath()).resolve(metadata.getId());

            // 先删除数据库
            this.metadataRepository.delete(metadata);
            // 再删除文件
            try {
                Files.deleteIfExists(fileFullPath);
            } catch (IOException ex) {
                log.warn("Deleting file exception, file: {} cause: {}", fileFullPath, ex.getMessage());
            }
        }, () -> {
            log.warn("Metadata not found: {}", objectId);
        });
    }

}
