package org.springseed.oss.minio.bean;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

/**
 * TODO
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
public class UserMetadata {
    private final static String DEFAULT_CONENET_TYPE = "application/octet-stream";
    public final static String FIELD_OBJECT_ID = "objectId";
    public final static String FIELD_BUCKET = "bucket";
    public final static String FIELD_MINIO_ID = "minioId";
    public final static String FIELD_FILE_NAME = "fileName";
    public final static String FIELD_FILE_SIZE = "fileSize";
    public final static String FIELD_CREATED_BY = "createdBy";
    public final static String FIELD_CREATED_ON = "createdOn";
    public final static String FIELD_CONTENT_TYPE = "contentType";

    private Map<String, String> values = new HashMap<>();

    public UserMetadata() {
    }

    public UserMetadata(Map<String, String> values) {
        this.values.putAll(values);
    }

    public static UserMetadata of() {
        return new UserMetadata();
    }
    
    public static UserMetadata of(Map<String, String> metadata) {
        return new UserMetadata(metadata);
    }

    public Map<String, String> toMap() {
        return new HashMap<>(this.values);
    }

    
    @Override
    public String toString() {
        return values.toString();
    }

    public UserMetadata objectId(String objectId) {
        if (hasText(objectId)) {
            this.values.put(FIELD_OBJECT_ID, objectId);
        }

        return this;
    }

    public UserMetadata bucket(String bucket) {
        if (hasText(bucket)) {
            this.values.put(FIELD_BUCKET, bucket);
        }

        return this;
    }

    public UserMetadata minioId(String minioId) {
        if (hasText(minioId)) {
            this.values.put(FIELD_MINIO_ID, minioId);
        }

        return this;
    }

    public UserMetadata fileName(String fileName) {
        if (hasText(fileName)) {
            this.values.put(FIELD_FILE_NAME, fileName);
        }

        return this;
    }

    public UserMetadata fileSize(long fileSize) {
        this.values.put(FIELD_FILE_SIZE, String.valueOf(fileSize));
        return this;
    }

    public UserMetadata createdBy(String createdBy) {
        if (hasText(createdBy)) {
            this.values.put(FIELD_CREATED_BY, createdBy);
        }

        return this;
    }

    public UserMetadata createdOn(ZonedDateTime createdOn) {
        if (createdOn != null) {
            this.values.put(FIELD_CREATED_ON, String.valueOf(createdOn.toEpochSecond()));
        }

        return this;
    }

    public UserMetadata conentType(String conentType) {
        if (! hasText(conentType)) {
            conentType = DEFAULT_CONENET_TYPE;
        }

        this.values.put(FIELD_CONTENT_TYPE, conentType);
        return this;
    }

    public String getObjectId() {
        return this.values.get(FIELD_OBJECT_ID);
    }

    public String getBucket() {
        return this.values.get(FIELD_BUCKET);
    }

    public String getMinioId() {
        return this.values.get(FIELD_MINIO_ID);
    }

    public String getCreatedBy() {
        return this.values.get(FIELD_CREATED_BY);
    }

    public String getFileName() {
        return this.values.get(FIELD_FILE_NAME);
    }

    public long getFileSize() {
        final String value = this.values.get(FIELD_FILE_SIZE);
        if(hasText(value)) {
            return Long.valueOf(value);
        }
        return 0;
    }

    public ZonedDateTime getCreatedOn() {
        final String value = this.values.get(FIELD_CREATED_ON);
        if(hasText(value)) {
            final Instant instant = Instant.ofEpochMilli(Long.valueOf(value));
            return ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
        }
        return null;
    }

    public String getContentType() {
        return this.values.get(FIELD_CONTENT_TYPE);
    }

    private boolean hasText(final String text) {
        return StringUtils.hasText(text);
    }
}
