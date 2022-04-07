package org.springseed.core.util.typeof;

import java.util.function.Consumer;

/**
 * https://github.com/nurkiewicz/typeof
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
public class ThenIs<S, T> extends CastObject<S, T> {
	final Then<S> parent;

	ThenIs(Then<S> parent, S object, Class<T> expectedType) {
		super(object, expectedType);
		this.parent = parent;
	}

	public Then<S> then(Consumer<T> thenBlock) {
		if (matchingType()) {
			thenBlock.accept(castObject());
			return new TerminalThen<>();
		}
		return parent;
	}
    
}
