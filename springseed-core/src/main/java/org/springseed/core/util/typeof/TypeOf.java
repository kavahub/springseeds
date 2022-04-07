package org.springseed.core.util.typeof;

/**
 * https://github.com/nurkiewicz/typeof
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
public class TypeOf {
    public static <S> WhenTypeOf<S> whenTypeOf(S object) {
		return new WhenTypeOf<>(object);
	}
}
