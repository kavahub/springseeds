package org.springseed.keycloak.config;

import javax.naming.NamingException;

import org.infinispan.manager.DefaultCacheManager;
import org.keycloak.Config;
import org.keycloak.cluster.ManagedCacheManagerProvider;
import org.springframework.jndi.JndiTemplate;

/**
 * 缓存管理器提供者
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
public class InfinispanCacheManagerProvider implements ManagedCacheManagerProvider {

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getCacheManager(Config.Scope config) {
        try {
            return (C) new JndiTemplate().lookup(DynamicJndiContextFactoryBuilder.JNDI_CACHE_MANAGAER, DefaultCacheManager.class);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }
    
}
