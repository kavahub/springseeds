package org.springseed.keycloak.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.Filter;
import javax.sql.DataSource;

import org.infinispan.configuration.parsing.ConfigurationBuilderHolder;
import org.infinispan.configuration.parsing.ParserRegistry;
import org.infinispan.manager.DefaultCacheManager;
import org.jboss.resteasy.plugins.server.servlet.HttpServlet30Dispatcher;
import org.jboss.resteasy.plugins.server.servlet.ResteasyContextParameters;
import org.keycloak.platform.Platform;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * TODO
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
@Configuration
@Slf4j
public class EmbeddedKeycloakConfig {
    @Bean("fixedThreadPool")
    public ExecutorService fixedThreadPool() {
        return Executors.newFixedThreadPool(5);
    }

    @Bean
    @ConditionalOnMissingBean(name = "springBootPlatform")
    protected SpringBootPlatformProvider springBootPlatform() {
        return (SpringBootPlatformProvider) Platform.getPlatform();
    }

    @Bean
    @ConditionalOnMissingBean(name = "springBeansJndiContextFactory")
    protected DynamicJndiContextFactoryBuilder springBeansJndiContextFactory(DataSource dataSource, DefaultCacheManager cacheManager, @Qualifier("fixedThreadPool") ExecutorService executorService) {
        return new DynamicJndiContextFactoryBuilder(dataSource, cacheManager, executorService);
    }

    @Bean
    @ConditionalOnMissingBean(name = "keycloakInfinispanCacheManager")
    protected DefaultCacheManager keycloakInfinispanCacheManager(KeycloakCustomProperties customProperties) throws Exception {

        KeycloakCustomProperties.Infinispan infinispan = customProperties.getInfinispan();
        Resource configLocation = infinispan.getConfigLocation();
        log.info("Using infinispan configuration from {}", configLocation.getURI());

        ConfigurationBuilderHolder configBuilder = new ParserRegistry().parse(configLocation.getURL());
        DefaultCacheManager defaultCacheManager = new DefaultCacheManager(configBuilder, false);
        defaultCacheManager.start();
        return defaultCacheManager;
    }
    
    @Bean
    @ConditionalOnMissingBean(name = "springBootConfigProvider")
    protected SpringBootConfigProvider springBootConfigProvider(KeycloakProperties keycloakProperties) {
        return new SpringBootConfigProvider(keycloakProperties);
    }
    
    @Bean
    @ConditionalOnMissingBean(name = "keycloakSessionManagement")
    protected FilterRegistrationBean<Filter> keycloakSessionManagement(KeycloakCustomProperties customProperties) {

        FilterRegistrationBean<Filter> filter = new FilterRegistrationBean<>();
        filter.setName("Keycloak Session Management");
        filter.setFilter(new KeycloakUndertowRequestFilter());
        filter.addUrlPatterns(customProperties.getServer().getContextPath() + "/*");

        return filter;
    }
    
    @Bean
    @ConditionalOnMissingBean(name = "keycloakJaxRsApplication")
    protected ServletRegistrationBean<HttpServlet30Dispatcher> keycloakJaxRsApplication(KeycloakCustomProperties customProperties) {

        initKeycloakEnvironmentFromProfiles();

        ServletRegistrationBean<HttpServlet30Dispatcher> servlet = new ServletRegistrationBean<>(new HttpServlet30Dispatcher());
        servlet.addInitParameter("javax.ws.rs.Application", EmbeddedKeycloakApplication.class.getName());

        servlet.addInitParameter("resteasy.allowGzip", "false");
        servlet.addInitParameter("keycloak.embedded", "true");
        servlet.addInitParameter(ResteasyContextParameters.RESTEASY_EXPAND_ENTITY_REFERENCES, "false");
        servlet.addInitParameter(ResteasyContextParameters.RESTEASY_SECURE_PROCESSING_FEATURE, "true");
        servlet.addInitParameter(ResteasyContextParameters.RESTEASY_DISABLE_DTDS, "true");
        servlet.addInitParameter(ResteasyContextParameters.RESTEASY_SERVLET_MAPPING_PREFIX, customProperties.getServer().getContextPath());
        servlet.addInitParameter(ResteasyContextParameters.RESTEASY_USE_CONTAINER_FORM_PARAMS, "false");
        servlet.addInitParameter(ResteasyContextParameters.RESTEASY_DISABLE_HTML_SANITIZER, "true");
        servlet.addUrlMappings(customProperties.getServer().getContextPath() + "/*");

        servlet.setLoadOnStartup(2);
        servlet.setAsyncSupported(true);

        return servlet;
    }

    private void initKeycloakEnvironmentFromProfiles() {
        final Resource profileResource = new ClassPathResource("keycloak-profile.properties");
        if (!profileResource.exists()) {
            if (log.isDebugEnabled()) {
                log.debug("Could not find keycloak-profile.properties on classpath.");
            }
            return;
        }

        log.info("Found keycloak-profile.properties on classpath.");
        try (InputStream in = profileResource.getInputStream()) {
            Properties profile = new Properties();
            profile.load(in);

            final String profilePrefix = "keycloak.profile.";
            for (Object key : profile.keySet()) {
                String value = (String) profile.get(key);
                String featureName = key.toString().toLowerCase();
                String currentValue = System.getProperty(profilePrefix + featureName);
                if (currentValue == null) {
                    System.setProperty(profilePrefix + featureName, value);
                }
            }
        } catch (IOException ioe) {
            log.warn("Could not read profile.properties.", ioe);
        }
    }

    @Bean
    ApplicationListener<ApplicationReadyEvent> onApplicationReadyEventListener(ServerProperties serverProperties,
            KeycloakCustomProperties keycloakCustomProperties) {

        return (evt) -> {

            Integer port = serverProperties.getPort();
            String keycloakContextPath = keycloakCustomProperties.getServer().getContextPath();

            log.info("Embedded Keycloak started: http://localhost:{}{}", port, keycloakContextPath);
        };
    }
    
}
