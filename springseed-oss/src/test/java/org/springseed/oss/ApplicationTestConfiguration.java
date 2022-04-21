package org.springseed.oss;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 测试配置
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration(exclude=SecurityAutoConfiguration.class)
public class ApplicationTestConfiguration {
    
}
