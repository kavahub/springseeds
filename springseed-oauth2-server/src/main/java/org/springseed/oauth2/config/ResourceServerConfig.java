package org.springseed.oauth2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * 
 * 资源服务配置
 *
 * @author PinWei Wan
 * @since 1.0.0
 */
@EnableResourceServer
@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()              
                .antMatchers("/error").permitAll()
                // 所有的请求必须有USERS角色
                .anyRequest().hasRole("USERS")
                .and()
                .httpBasic();
    }
    
}
