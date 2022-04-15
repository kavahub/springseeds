package org.springseed.resource.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * bean配置
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@Configuration
public class BeanConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
