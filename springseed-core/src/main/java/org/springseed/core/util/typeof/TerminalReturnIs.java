package org.springseed.core.util.typeof;

import java.util.function.Function;

/**
 * https://github.com/nurkiewicz/typeof
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
public class TerminalReturnIs<S, T, R> extends ReturnIs<S, T, R> {

	private final R result;

	public TerminalReturnIs(S object, R result) {
		super(object, null);
		this.result = result;
	}

	@Override
	public ThenReturn<S, R> thenReturn(Function<T, R> resultFun) {
		return new TerminalThenReturn<>(object, result);
	}

    @Override
    public ThenReturn<S, R> thenReturn(R result) {
        return new TerminalThenReturn<>(object, this.result);
    }
    
}
