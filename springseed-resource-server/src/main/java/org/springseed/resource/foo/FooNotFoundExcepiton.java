package org.springseed.resource.foo;

import org.springseed.resource.util.CommonRuntimeException;

/**
 * TODO
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
public class FooNotFoundExcepiton extends CommonRuntimeException {

    public FooNotFoundExcepiton(String message) {
        super(message);
    }
    
}
