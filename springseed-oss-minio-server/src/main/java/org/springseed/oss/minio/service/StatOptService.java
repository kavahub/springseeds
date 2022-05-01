package org.springseed.oss.minio.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springseed.oss.minio.bean.MinioUserMetadata;
import org.springseed.oss.minio.util.MinioArgsUtils;
import org.springseed.oss.minio.util.MinioExceptionUtil;
import org.springseed.oss.minio.util.MinioUtils;
import org.springseed.oss.minio.util.OSSMinioNotFoundException;

import io.minio.MinioClient;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * stat操作
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
@Slf4j
@Service
public class StatOptService {
    @Autowired
    private MinioClient client;

    public boolean exist(final String bucket, final String objectId) {
        boolean found = false;
        try {
            getStatObjectResponse(bucket, objectId);
            found = true;
        }catch(OSSMinioNotFoundException e) { 
        }

        return found;
    }

    public MinioUserMetadata getUserMetadata(final String bucket, final String objectId,
            final Consumer<StatObjectArgs.Builder> consumer) {
        final StatObjectResponse statObjectResponse = this.getStatObjectResponse(bucket, objectId, consumer);
        return MinioUserMetadata.of(statObjectResponse.userMetadata());
    }

    public MinioUserMetadata getUserMetadata(final String bucket, final String objectId) {
        final StatObjectResponse statObjectResponse = this.getStatObjectResponse(bucket, objectId);
        return MinioUserMetadata.of(statObjectResponse.userMetadata());
    }

    public List<MinioUserMetadata> getUserMetadata(final String bucket, final Iterable<String> objectIds) {
        return StreamSupport.stream(objectIds.spliterator(), false)
                .map(objectId -> getUserMetadata(bucket, objectId))
                .collect(Collectors.toList());
    }

    public StatObjectResponse getStatObjectResponse(final String bucket, final String objectId,
            final Consumer<StatObjectArgs.Builder> consumer) {
        final String minioId = MinioUtils.minioId(objectId);
        try {
            final StatObjectArgs.Builder builder = StatObjectArgs.builder()
                    .bucket(bucket)
                    .object(minioId);
            if (consumer != null) {
                consumer.accept(builder);
            }

            final StatObjectArgs args = builder.build();
            if (log.isDebugEnabled()) {
                log.debug("StatObjectArgs={}", MinioArgsUtils.printStatObjectArgs(args));
            }
            return client.statObject(args);
        } catch (Exception e) {
            throw MinioExceptionUtil.wapper(e);
        }
    }

    public StatObjectResponse getStatObjectResponse(final String bucket, final String objectId) {
        return getStatObjectResponse(bucket, objectId, null);
    }

    public Map<String, StatObjectResponse> getStatObjectResponse(final String bucket,
            final Iterable<String> objectIds) {
        return StreamSupport.stream(objectIds.spliterator(), false)
                .map(objectId -> new HashMap.SimpleEntry<>(objectId, getStatObjectResponse(bucket, objectId)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
