package org.springseed.core.util.typeof;

/**
 * TODO
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
public class CastObject<S, T> {
    protected final S object;
    protected final Class<T> expectedType;

    public CastObject(S object, Class<T> expectedType) {
        this.object = object;
        this.expectedType = expectedType;
    }

    @SuppressWarnings("unchecked")
    protected T castObject() {
		return (T) object;
	}

    protected boolean matchingType() {
		return object != null && expectedType.isAssignableFrom(object.getClass());
	}
}
