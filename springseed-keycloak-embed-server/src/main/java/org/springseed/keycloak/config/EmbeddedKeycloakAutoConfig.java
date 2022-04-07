package org.springseed.keycloak.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 自动配置
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties({KeycloakProperties.class, KeycloakCustomProperties.class})
public class EmbeddedKeycloakAutoConfig {
    
}
