package org.springseed.core.util.typeof;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * TODO
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
public class FirstIs<S, T> extends CastObject<S, T> {
	final Then<S> parent;

	FirstIs(Then<S> parent, S object, Class<T> expectedType) {
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

	public <R> ThenReturn<S, R> thenReturn(Function<T, R> result) {
		if (matchingType()) {
			return new TerminalThenReturn<>(object, result.apply(castObject()));
		}
		return new ThenReturn<>(object);
	}

	public <R> ThenReturn<S, R> thenReturn(R result) {
		if (matchingType()) {
			return new TerminalThenReturn<>(object, result);
		}
		return new ThenReturn<>(object);
	}
    
}
