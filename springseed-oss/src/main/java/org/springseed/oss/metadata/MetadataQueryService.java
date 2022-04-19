package org.springseed.oss.metadata;

import java.util.Optional;

import org.springframework.stereotype.Service;

/**
 * 元数据查询服务
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@Service
public class MetadataQueryService {
    private final MetadataRepository metadataRepository;

    public MetadataQueryService(MetadataRepository metadataRepository) {
        this.metadataRepository = metadataRepository;
    }

    /**
     * 依据ID查找元数据，如果不存在，抛出 {@link MetadataNotFoundException} 异常
     * 
     * @param id
     * @return
     */
    public Optional<Metadata> findById(final String id) {
        final Metadata entity = metadataRepository.findById(id).orElseThrow(() -> new MetadataNotFoundException(String.valueOf(id)));
        return Optional.of(entity);
    } 
}
