package org.springseed.oss.local.metadata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springseed.core.util.FileUtils;
import org.springseed.oss.local.util.OSSLocalRuntimeException;
import org.springseed.oss.local.util.SecurityUtils;


/**
 * 元数据保持
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
@Service
@Transactional()
public class MetadataSaveService {
    public final static String UN_NAMED_FILE = "未命名文件";

    @Autowired
    private MetadataRepository metadataRepository;
    @Autowired
    private MetadataQueryService metadataQueryService;

    public Metadata save(final String name, final String path, final long size) {
        String tmpName = name;
        if (!StringUtils.hasText(tmpName)) {
            tmpName = UN_NAMED_FILE;
        }

        final Metadata metadata = Metadata.builder().path(path)
                .name(tmpName).size(size).type(FileUtils.getFileExtension(tmpName))
                .createdBy(SecurityUtils.getCurrentUserInfo()).build();
        return metadataRepository.save(metadata);
    }

    public void updateName(final String id, final String name) {
        String tmpName = name;
        if (!StringUtils.hasText(tmpName)) {
            tmpName = UN_NAMED_FILE;
        }

        final Metadata metadata = this.metadataQueryService.findById(id).get();
        final String createdBy = metadata.getCreatedBy();
        if (StringUtils.hasText(createdBy) && !metadata.getCreatedBy().equals(SecurityUtils.getCurrentUserInfo())) {
            throw new OSSLocalRuntimeException("创建人才能修改");
        }
        ;
        metadata.setName(tmpName);
        metadata.setType(FileUtils.getFileExtension(tmpName));
        metadataRepository.save(metadata);
    }
}
