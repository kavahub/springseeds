package org.springseed.oss.local.metadata;

import java.io.File;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;

import org.springseed.core.util.SnowflakeUtil;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 元数据
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "SSD_METADATA")
public class Metadata {
    public final static String PATH_SEP = ",";
    @Id
    @EqualsAndHashCode.Include
    private String id;

    /** 文件路径 */
    @Column(updatable = false, nullable = false)
    private String path;

    /** 类型 */
    private String type;

    /** 名称 */
    @Column(nullable = false)
    private String name;

    /** 大小，字节 */
    private long size;

    /** 创建日期 */
    private Date createdOn;

    /** 创建人 */
    private String createdBy;

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = SnowflakeUtil.INSTANCE.nextIdStr();
        }

        if (this.createdOn == null) {
            this.createdOn = new Date();
        }
    }

    public String getFullPath() {
        return this.path.replace(PATH_SEP, File.separator);
    }

    public static String joinPath(String[] paths) {
        return String.join(PATH_SEP, paths);
    }
}
