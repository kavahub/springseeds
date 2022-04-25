package org.springseed.keycloak.config;

import java.util.HashMap;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * 属性
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "springseed.keycloak")
public class KeycloakProperties extends HashMap<String, Object>  {
    
}
