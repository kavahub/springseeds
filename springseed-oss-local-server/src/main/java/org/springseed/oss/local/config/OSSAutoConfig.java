package org.springseed.oss.local.config;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

/**
 * 自动配置
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties({OSSProperties.class})
@Slf4j
public class OSSAutoConfig {
    
    @Bean
    ApplicationListener<ApplicationReadyEvent> onApplicationReadyEventListener(ServerProperties serverProperties) {

        return (evt) -> {

            final Integer port = serverProperties.getPort();
            final String content = serverProperties.getServlet().getContextPath();

            log.info("LocalOSSApplication started: http://localhost:{}{}", port, content == null ? "" : content);
        };
    }    
}
