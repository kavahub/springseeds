package org.springseed.oss.minio.service;

import java.io.InputStream;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springseed.oss.minio.bean.UserMetadata;
import org.springseed.oss.minio.util.MinioArgsUtils;
import org.springseed.oss.minio.util.MinioExceptionUtil;
import org.springseed.oss.minio.util.MinioUtils;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;

/**
 * TODO
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
@Slf4j
@Service
public class PutOptService {
    @Autowired
    private MinioClient client;

    /**
     * 上传一个文件流
     * 
     * @param bucket
     * @param objectId
     * @param file        文件流
     * @param contentType
     * @param consumer
     */
    public void put(final UserMetadata metadata, final InputStream file, final Consumer<PutObjectArgs.Builder> consumer) {
        final String minioId = MinioUtils.minioId(metadata.getObjectId());
        metadata.minioId(minioId);

        try {
            final PutObjectArgs.Builder builder = PutObjectArgs.builder()
                    .bucket(metadata.getBucket())
                    .object(metadata.getMinioId())
                    .userMetadata(metadata.toMap())
                    .stream(file, file.available(), -1)
                    .contentType(metadata.getContentType());
            if (consumer != null) {
                consumer.accept(builder);
            }

            final PutObjectArgs args = builder.build();
            if (log.isDebugEnabled()) {
                log.debug("PutObjectArgs={}", MinioArgsUtils.printPutObjectArgs(args));
            }
            client.putObject(args);
        } catch (Exception e) {
            throw MinioExceptionUtil.wapper(e);
        }
    }

    public void put(final UserMetadata metadata, final InputStream file) {
        this.put(metadata, file);
    }

}
