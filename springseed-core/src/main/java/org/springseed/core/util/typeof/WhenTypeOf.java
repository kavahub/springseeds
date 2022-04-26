package org.springseed.core.util.typeof;

/**
 * https://github.com/nurkiewicz/typeof
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
public class WhenTypeOf<S> {

	private S object;

	WhenTypeOf(S object) {
		this.object = object;
	}

	public <T> FirstIs<S, T> is(Class<T> expectedType) {
		return new FirstIs<>(new Then<>(object), object, expectedType);
	}
    
}
