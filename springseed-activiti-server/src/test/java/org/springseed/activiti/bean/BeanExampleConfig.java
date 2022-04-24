package org.springseed.activiti.bean;

import org.activiti.api.process.runtime.connector.Connector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

/**
 * TODO
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@Configuration
@Slf4j
public class BeanExampleConfig {
    @Bean
    public Connector tagTextConnectorBean() {
        return integrationContext -> {
            Content contentToTag = (Content) integrationContext.getInBoundVariables().get("content");
            contentToTag.getTags().add(" :) ");
            integrationContext.addOutBoundVariable("content",
                    contentToTag);
            log.info("Final Content: " + contentToTag);
            return integrationContext;
        };
    }

    @Bean
    public Connector discardTextConnectorBean() {
        return integrationContext -> {
            Content contentToDiscard = (Content) integrationContext.getInBoundVariables().get("content");
            contentToDiscard.getTags().add(" :( ");
            integrationContext.addOutBoundVariable("content",
                    contentToDiscard);
                    log.info("Final Content: " + contentToDiscard);
            return integrationContext;
        };
    }   
}
