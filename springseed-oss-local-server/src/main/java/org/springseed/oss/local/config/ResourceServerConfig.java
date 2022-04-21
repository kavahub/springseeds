package org.springseed.oss.local.config;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

/**
 * 资源服务器
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
@Configuration
public class ResourceServerConfig extends WebSecurityConfigurerAdapter {

	// @formatter:off
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors()
		.and()
		.csrf().disable()
          .authorizeRequests()
            .antMatchers(HttpMethod.GET)
              .hasAnyAuthority("ROLE_oss_read", "SCOPE_oss_read")
            .antMatchers(HttpMethod.POST)
              .hasAnyAuthority("ROLE_oss_write", "SCOPE_oss_write")
            .antMatchers(HttpMethod.DELETE)
              .hasAnyAuthority("ROLE_oss_delete", "SCOPE_oss_delete")              
            .anyRequest()
              .authenticated()
		.and()
			.oauth2ResourceServer()
				.jwt();
	}
	// @formatter:on

	@Bean
	JwtDecoder jwtDecoder(OAuth2ResourceServerProperties properties) {
		return NimbusJwtDecoder.withJwkSetUri(properties.getJwt().getJwkSetUri()).build();
	}

}
