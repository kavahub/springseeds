package org.springseed.oauth2.operator;

import org.springseed.oauth2.util.CommonRuntimeException;

/**
 * 
 * 操作员不存在异常
 *
 * @author PinWei Wan
 * @since 1.0.0
 */
public class OperatorNotFoundExcepiton extends CommonRuntimeException {

    public OperatorNotFoundExcepiton(String message) {
        super(message);
    }
    
}
