package org.springseed.oauth2.operator;

import org.springseed.oauth2.util.CommonRuntimeException;

/**
 * 
 * 手机号校验异常
 *
 * @author PinWei Wan
 * @since 1.0.0
 */
public class PhoneNumberValidateExcepiton extends CommonRuntimeException {

    public PhoneNumberValidateExcepiton(String phoneNumber) {
        super(phoneNumber);
    }
    
}
