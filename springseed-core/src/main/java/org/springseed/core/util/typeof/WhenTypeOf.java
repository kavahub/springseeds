package org.springseed.core.util.typeof;

/**
 * TODO
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
public class WhenTypeOf<S> {

	private S object;

	WhenTypeOf(S object) {
		this.object = object;
	}

	public <T> FirstIs<S, T> is(Class<T> type) {
		return new FirstIs<>(new Then<>(object), object, type);
	}
    
}
