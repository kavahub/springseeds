package org.springseed.oss.local;

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
import org.springseed.oss.local.config.OSSProperties;
import org.springseed.oss.local.util.FileNotFoundException;
import org.springseed.oss.local.util.FileReadException;
import org.springseed.oss.metadata.Metadata;
import org.springseed.oss.metadata.MetadataQueryService;
import org.springseed.oss.metadata.MetadataRepository;
import org.springseed.oss.util.OSSRuntimeException;
import org.springseed.oss.util.OSSUtil;
import org.springseed.oss.util.SecurityUtils;

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
                throw new OSSRuntimeException("Failed to store empty file.");
            }

            if (log.isDebugEnabled()) {
                log.debug("Begin to store file: {}", file.getOriginalFilename());
            }

            try (InputStream fileData = file.getInputStream()) {
                final String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                final long fileSize = file.getSize();
                final String fileType = OSSUtil.getFileType(fileName);
                final String filePath = OSSUtil.getFilePath(fileName);

                final Path destinationPath = this.uploadRootPath.resolve(filePath);
                if (!Files.exists(destinationPath)) {
                    Files.createDirectories(destinationPath);
                }

                // 存储文件元数据
                final Metadata metadata = Metadata.builder().path(filePath)
                        .name(fileName).size(fileSize).type(fileType)
                        .createdBy(SecurityUtils.getCurrentUserInfo().orElse(null)).build();
                metadataRepository.save(metadata);

                
                // 存储文件
                final long size = Files.copy(fileData, destinationPath.resolve(metadata.getId()), StandardCopyOption.REPLACE_EXISTING);
                if (log.isDebugEnabled()) {
                    log.debug("File size : {}", size);
                }
                return metadata.getId();
            }

        } catch (IOException e) {
            throw new OSSRuntimeException("Failed to store file", e);
        }
    }

    /**
     * 读取文件
     * 
     * @param fileId 元数据ID
     * @return
     */
    public Resource loadByMetadataId(final String metadataId) {
        return this.metadataQueryService.findById(metadataId)
                .map(this::loadByMetadata)
                .get();
    }

    /**
     * 批量读取文件
     * 
     * @param metadataIds 元数据ID列表
     * @return
     */
    public List<Resource> loadByMetadataIds(final List<String> metadataIds) {
        final List<Metadata> metadatas = this.metadataRepository.findAllById(metadataIds);
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
            log.debug("Begin to read file: {}", metadata.getId());
        }

        final Path fileFullPath = this.uploadRootPath.resolve(metadata.getPath()).resolve(metadata.getId());
        try {
            final Resource resource = new UrlResource(fileFullPath.toUri());

            if (!resource.exists()) {
                throw new FileNotFoundException(fileFullPath.toString());
            }
            
            if (!resource.isReadable()) {
                throw new FileReadException(fileFullPath.toString());
            } 

            return resource;
        } catch (MalformedURLException e) {
            throw new FileReadException(e);
        }
    }

    /**
     * 删除文件
     * 
     * @param id 元数据ID
     */
    @Transactional
    public void removeByMetadataId(final String metadataId) {
        this.metadataRepository.findById(metadataId).ifPresentOrElse(metadata -> {
            if (log.isDebugEnabled()) {
                log.debug("Begin to remove file: {}", metadata.getName());
            }

            final Path fileFullPath = this.uploadRootPath.resolve(metadata.getPath()).resolve(metadata.getId());

            // 先删除数据库
            this.metadataRepository.delete(metadata);
            // 再删除文件
            try {
                Files.deleteIfExists(fileFullPath);
            } catch (IOException ex) {
                log.warn("Deleting file exception, file: {} cause: {}", fileFullPath, ex.getMessage());
            }
        }, () -> {
            log.warn("Metadata not found: {}", metadataId);
        });
    }

}
