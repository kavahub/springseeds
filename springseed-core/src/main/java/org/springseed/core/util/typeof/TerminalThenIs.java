package org.springseed.core.util.typeof;

import java.util.function.Consumer;

/**
 * TODO
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
public class TerminalThenIs<S, T> extends ThenIs<S, T> {

	TerminalThenIs(Then<S> parent, S object, Class<T> expectedType) {
		super(parent, object, expectedType);
	}

	@Override
	public Then<S> then(Consumer<T> thenBlock) {
		return parent;
	}
    
}
