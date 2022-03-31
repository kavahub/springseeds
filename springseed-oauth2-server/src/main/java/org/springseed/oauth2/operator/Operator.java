package org.springseed.oauth2.operator;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 * 
 * 操作员
 *
 * @author PinWei Wan
 * @since 1.0.0
 */
@Data
@Entity(name = "SBS_OPERATOR")
@GenericGenerator(name = "UUID", strategy = "uuid")
public class Operator implements Serializable {

    /** 用户ID */
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(length = 32)
    private String id;

    @Version
    private Integer version;

    /** 用户名 */
    @Column(length = 32, unique = true, updatable = false, nullable = false)
    private String username;

    /** 密码 */
    @Column
    private String password;

    /** 昵称 */
    @Column
    private String nickname;

    /** 邮箱 */
    @Column
    private String email;

    /** 手机号 */
    @Column
    private String phoneNumber;

    public Operator() {
    }

    public Operator(String username) {
        this.username = username;
    }

    public Operator(String username, String nickname, String email, String phoneNumber) {
        this.username = username;
        this.email = email;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
    }

    @PrePersist
    @PreUpdate
    public void setDefualt() {
        if (username != null) {
            username = username.toUpperCase();
        }
        if (version == null) {
            version = 0;
        }
    }
}
