package org.springseed.core.util.typeof;

import java.util.function.Function;

/**
 * TODO
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
public class TerminalThenReturn<S, R> extends ThenReturn<S, R> {

	private final R result;

	public TerminalThenReturn(S object, R result) {
		super(object);
		this.result = result;
	}

	@Override
	public <T> ReturnIs<S, T, R> is(Class<T> expectedType) {
		return new TerminalReturnIs<>(object, this.result);
	}

	@Override
	public R get() {
		return this.result;
	}

	@Override
	public R orElse(R result) {
		return this.result;
	}
	
	@Override
	public R orElse(Function<S, R> resultFun) {
	    return this.result;
	}
    
}
