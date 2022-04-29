package org.springseed.oss.minio.util;

import org.springframework.util.Assert;
import org.springseed.core.util.PathUtils;

import lombok.experimental.UtilityClass;

/**
 * TODO
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
@UtilityClass
public class MinioUtils {
    public String minioId(final String objectId) {
        Assert.hasText(objectId, "Argument 'objectId' must not be empty");

        final String[] paths = PathUtils.generalHashPath(objectId); 
        return new StringBuilder("/").append(String.join("/", paths)).append("/").append(objectId).toString();
    }
}
