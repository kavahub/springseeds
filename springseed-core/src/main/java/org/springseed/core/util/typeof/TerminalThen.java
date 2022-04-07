package org.springseed.core.util.typeof;

import java.util.function.Consumer;

/**
 * https://github.com/nurkiewicz/typeof
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
public class TerminalThen<S> extends Then<S> {

	public TerminalThen() {
		super(null);
	}

	@Override
	public <T> ThenIs<S, T> is(Class<T> type) {
		return new TerminalThenIs<>(this, null, null);
	}

	@Override
	public void orElse(Consumer<S> orElseBlock) {
		//no-op
	}
    
}
