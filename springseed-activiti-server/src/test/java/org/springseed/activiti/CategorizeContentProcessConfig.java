package org.springseed.activiti;

import java.util.Map;

import org.activiti.api.process.runtime.connector.Connector;
import org.activiti.api.process.runtime.events.ProcessCompletedEvent;
import org.activiti.api.process.runtime.events.listener.ProcessRuntimeEventListener;
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
public class CategorizeContentProcessConfig {
    @Bean
    public Connector processTextConnector() {
        return integrationContext -> {
            Map<String, Object> inBoundVariables = integrationContext.getInBoundVariables();
            String contentToProcess = (String) inBoundVariables.get("content");
            // Logic Here to decide if content is approved or not
            if (contentToProcess.contains("activiti")) {
                log.info("> Approving content: " + contentToProcess);
                integrationContext.addOutBoundVariable("approved",
                        true);
            } else {
                log.info("> Discarding content: " + contentToProcess);
                integrationContext.addOutBoundVariable("approved",
                        false);
            }
            return integrationContext;
        };
    }

    @Bean
    public Connector tagTextConnector() {
        return integrationContext -> {
            String contentToTag = (String) integrationContext.getInBoundVariables().get("content");
            contentToTag += " :) ";
            integrationContext.addOutBoundVariable("content",
                    contentToTag);
                    log.info("Final Content: " + contentToTag);
            return integrationContext;
        };
    }

    @Bean
    public Connector discardTextConnector() {
        return integrationContext -> {
            String contentToDiscard = (String) integrationContext.getInBoundVariables().get("content");
            contentToDiscard += " :( ";
            integrationContext.addOutBoundVariable("content",
                    contentToDiscard);
                    log.info("Final Content: " + contentToDiscard);
            return integrationContext;
        };
    }

    @Bean
    public ProcessRuntimeEventListener<ProcessCompletedEvent> processCompletedListener() {
        return processCompleted -> log.info(">>> Process Completed: '"
                + processCompleted.getEntity().getName() +
                "' We can send a notification to the initiator: " + processCompleted.getEntity().getInitiator());
    }


}
