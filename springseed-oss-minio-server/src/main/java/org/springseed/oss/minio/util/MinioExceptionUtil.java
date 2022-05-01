package org.springseed.oss.minio.util;

import org.springseed.core.util.typeof.TypeOf;

import io.minio.errors.ErrorResponseException;
import io.minio.errors.MinioException;
import lombok.experimental.UtilityClass;

/**
 * 异常工具
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@UtilityClass
public class MinioExceptionUtil {
    public RuntimeException wapper(Exception ex) {
        if (! (ex instanceof MinioException)) {
            // 不是Minio异常
            throw new OSSMinioInternalException(ex);
        }

        final RuntimeException exception = TypeOf.whenTypeOf(ex)
        .is(ErrorResponseException.class).thenReturn(errorResponseException -> {
            String code = errorResponseException.errorResponse().code();
            if ("AccessDenied".equalsIgnoreCase(code)) {
                return new OSSMinioInternalException("无权访问Minio服务：" + errorResponseException.getMessage());
            } else if ("NoSuchBucket".equalsIgnoreCase(code)) {
                return new OSSMinioException("桶不存在：" + errorResponseException.getMessage());
            } else if ("InvalidBucketName".equalsIgnoreCase(code)) {
                return new OSSMinioException("无效的桶名称：" + errorResponseException.getMessage());
            } else if ("NoSuchKey".equalsIgnoreCase(code)) {
                return new OSSMinioNotFoundException("对象标识不存在：" + errorResponseException.getMessage());
            } else if ("NoSuchObject".equalsIgnoreCase(code)) {
                return new OSSMinioNotFoundException("对象不存在：" + errorResponseException.getMessage());
            } else {
                return new OSSMinioInternalException(errorResponseException);
            }

        }).orElse(e -> new OSSMinioInternalException(e));

        return exception;
    }
}
