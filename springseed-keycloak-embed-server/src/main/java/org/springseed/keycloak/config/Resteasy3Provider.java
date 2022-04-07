package org.springseed.keycloak.config;

import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.keycloak.common.util.ResteasyProvider;

/**
 * 继承{@link ResteasyProvider}
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
public class Resteasy3Provider implements ResteasyProvider {

    @Override
    public <R> R getContextData(Class<R> type) {
        ResteasyProviderFactory.getInstance();
        return ResteasyProviderFactory.getContextData(type);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void pushDefaultContextObject(Class type, Object instance) {
        ResteasyProviderFactory.getInstance();
        ResteasyProviderFactory.getContextData(Dispatcher.class)
            .getDefaultContextObjects()
            .put(type, instance);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void pushContext(Class type, Object instance) {
        ResteasyProviderFactory.getInstance();
        ResteasyProviderFactory.pushContext(type, instance);
    }

    @Override
    public void clearContextData() {
        ResteasyProviderFactory.getInstance();
        ResteasyProviderFactory.clearContextData();
    }
    
}
