package org.springseed.oauth2.operator;

import org.springseed.oauth2.util.CommonRuntimeException;

/**
 * 
 * 邮箱校验异常
 *
 * @author PinWei Wan
 * @since 1.0.0
 */
public class EmailValidateExcepiton extends CommonRuntimeException {

    public EmailValidateExcepiton(String email) {
        super(email);
    }
    
}
