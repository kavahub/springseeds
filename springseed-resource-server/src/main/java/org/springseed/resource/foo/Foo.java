package org.springseed.resource.foo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;

import org.springseed.core.util.SnowflakeUtil;

import lombok.Data;

/**
 * 实体
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@Entity
@Data
public class Foo{
    @Id
    @Column(length = 32)
    private String id;

    private String name;

    protected Foo() {
    }

    public Foo(String name) {
        this.name = name;
    } 
    
    @PrePersist
    public void prePersist() {
        SnowflakeUtil util = SnowflakeUtil.INSTANCE;
        this.setId(util.nextIdStr());
    }
}
