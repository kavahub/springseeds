package org.springseed.activiti.task;

import org.activiti.api.task.runtime.events.TaskAssignedEvent;
import org.activiti.api.task.runtime.events.TaskCompletedEvent;
import org.activiti.api.task.runtime.events.listener.TaskRuntimeEventListener;
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
public class TaskExampleConfig {
    @Bean
    public TaskRuntimeEventListener<TaskAssignedEvent> taskAssignedListener() {
        return taskAssigned -> log.info(">>> Task Assigned: '"
                + taskAssigned.getEntity().getName() +
                "' We can send a notification to the assginee: " + taskAssigned.getEntity().getAssignee());
    }

    @Bean
    public TaskRuntimeEventListener<TaskCompletedEvent> taskCompletedListener() {
        return taskCompleted -> log.info(">>> Task Completed: '"
                + taskCompleted.getEntity().getName() +
                "' We can send a notification to the owner: " + taskCompleted.getEntity().getOwner());
    }   
}
