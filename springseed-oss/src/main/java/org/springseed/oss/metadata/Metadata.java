package org.springseed.oss.metadata;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.springseed.core.util.SnowflakeUtil;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 元数据
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */

@Data
@Builder
@Entity(name = "OSS_METADATA")
@Table(indexes = {
        @Index(name = "idx_path", columnList = "path")
})
public class Metadata {
    private final static String PATH_CHAR = ",";

    @Id
    @Column(length = 32)
    @EqualsAndHashCode.Include
    private String id;

    /** 文件路径 */
    @Column(updatable = false, nullable = false)
    private String path;

    /** 类型 */
    private String type;

    /** 名称 */
    private String name;

    /** 大小，字节 */
    private long size;

    /** 创建日期 */
    private Date createdOn;

    /** 创建人 */
    private String creaateBy;

    /** 校验码 */
    private String crc32;

    public static String joinPath(final String[] path) {
        return String.join(PATH_CHAR, path);
    }

    public String[] splitPath() {
        return this.path.split(PATH_CHAR);
    }

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = SnowflakeUtil.INSTANCE.nextIdStr();
        }

        if (this.createdOn == null) {
            this.createdOn = new Date();
        }
    }
}
