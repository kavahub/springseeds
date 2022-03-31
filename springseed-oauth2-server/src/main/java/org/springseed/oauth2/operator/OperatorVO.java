package org.springseed.oauth2.operator;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * 
 * 操作员VO
 *
 * @author PinWei Wan
 * @since 1.0.0
*/
@Data
public class OperatorVO implements Serializable {

    /** 用户ID */
    private String id;

    private Integer version;

    /** 用户名 */
    @NotBlank
    private String username;

    /** 密码 */
    @NotBlank
    private String password;

    /** 昵称 */
    @NotBlank
    private String nickname;

    /** 邮箱 */
    @NotBlank
    private String email;

    /** 手机号 */
    @NotBlank
    private String phoneNumber;
}
