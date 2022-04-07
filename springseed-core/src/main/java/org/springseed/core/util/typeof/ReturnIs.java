package org.springseed.core.util.typeof;

import java.util.function.Function;

/**
 * TODO
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
public class ReturnIs<S, T, R> extends CastObject<S, T> {
	public ReturnIs(S object, Class<T> expectedType) {
		super(object, expectedType);
	}

	public ThenReturn<S, R> thenReturn(Function<T, R> resultFun) {
		if (matchingType()) {
			final R result = resultFun.apply(castObject());
			return new TerminalThenReturn<>(object, result);
		}
		return new ThenReturn<>(object);
	}

	public ThenReturn<S, R> thenReturn(R result) {
		if (matchingType()) {
			return new TerminalThenReturn<>(object, result);
		}
		return new ThenReturn<>(object);
	}
    
}
