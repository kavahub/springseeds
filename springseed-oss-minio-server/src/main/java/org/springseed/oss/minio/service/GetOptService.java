package org.springseed.oss.minio.service;

import java.io.InputStream;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springseed.oss.minio.config.OSSMinioProperties;
import org.springseed.oss.minio.util.MinioArgsUtils;
import org.springseed.oss.minio.util.MinioExceptionUtil;
import org.springseed.oss.minio.util.MinioUtils;

import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;

/**
 * TODO
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
@Slf4j
@Service
public class GetOptService {
    @Autowired
    private MinioClient client;
    @Autowired
    private OSSMinioProperties properties;

    public InputStream getObject(final String bucket, final String objectId, final Consumer<GetObjectArgs.Builder> consumer) {
        final String minioId = MinioUtils.minioId(objectId);
        try {
            final GetObjectArgs.Builder builder = GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(minioId);

            if (consumer != null) {
                consumer.accept(builder);
            }

            final GetObjectArgs args = builder.build();
            if (log.isDebugEnabled()) {
                log.debug("GetObjectArgs={}", MinioArgsUtils.printGetObjectArgs(args));
            }

            return client.getObject(builder.build());
        } catch (Exception e) {
            throw MinioExceptionUtil.wapper(e);
        }
    }

    public InputStream getObject(final String bucket, final String objectId) {
        return getObject(bucket, objectId, null);
    }

    public String getPresignedObjectUrl(final String bucket, final String objectId, final Method method,
            final Consumer<GetPresignedObjectUrlArgs.Builder> consumer) {
        final String minioId = MinioUtils.minioId(objectId);
        try {
            final GetPresignedObjectUrlArgs.Builder builder = GetPresignedObjectUrlArgs.builder()
                    .bucket(bucket)
                    .object(minioId)
                    .method(method);

            /** 使用配置数据 */
            switch (method) {
                case GET:
                    builder.expiry((int) properties.getMethod().getGetExpiry().toSeconds());
                    break;
                case POST:
                    builder.expiry((int) properties.getMethod().getPostExpiry().toSeconds());
                    break;
                case DELETE:
                    builder.expiry((int) properties.getMethod().getDeleteExpiry().toSeconds());
                    break;
                default:
                    break;
            }

            if (consumer != null) {
                consumer.accept(builder);
            }

            final GetPresignedObjectUrlArgs args = builder.build();
            if (log.isDebugEnabled()) {
                log.debug("GetPresignedObjectUrlArgs={}", MinioArgsUtils.printGetPresignedObjectUrlArgs(args));
            }
            return client.getPresignedObjectUrl(args);
        } catch (Exception e) {
            throw MinioExceptionUtil.wapper(e);
        }
    }

    public String getPresignedObjectUrl(final String bucket, final String objectId, final Method method) {
        return getPresignedObjectUrl(bucket, objectId, method, null);
    }
}
