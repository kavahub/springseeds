package org.springseed.keycloak.config;

import java.io.IOException;
import java.util.List;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * 加载默认属性监听器
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@Slf4j
public class PopulateKeycloakPropertiesApplicationListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {

        ApplicationEnvironmentPreparedEvent envEvent = (ApplicationEnvironmentPreparedEvent) event;
        ConfigurableEnvironment env = envEvent.getEnvironment();

        try {
            Resource resource = new ClassPathResource("META-INF/keycloak-defaults.yml");

            if (!resource.exists()) {
                return;
            }

            log.info("Loading default keycloak properties configuration from: {}", resource.getURI());

            YamlPropertySourceLoader sourceLoader = new YamlPropertySourceLoader();
            List<PropertySource<?>> yamlTestProperties = sourceLoader.load("keycloak-defaults.yml", resource);
            if (!yamlTestProperties.isEmpty()) {
                env.getPropertySources().addLast(yamlTestProperties.get(0));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
}
