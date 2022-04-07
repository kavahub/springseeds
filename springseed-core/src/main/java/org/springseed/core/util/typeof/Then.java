package org.springseed.core.util.typeof;

import java.util.function.Consumer;

/**
 * https://github.com/nurkiewicz/typeof
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
public class Then<S> {

	private final S object;

	Then(S object) {
		this.object = object;
	}

	public <T> ThenIs<S, T> is(Class<T> type) {
		return new ThenIs<>(this, object, type);
	}

	public void orElse(Consumer<S> orElseBlock) {
		orElseBlock.accept(object);
	}
    
}
