package org.springseed.oss.local;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

/**
 * TODO
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
          .authorizeRequests()
            .antMatchers(HttpMethod.GET)
              .hasAuthority("SCOPE_oss_read")
            .antMatchers(HttpMethod.POST)
              .hasAuthority("SCOPE_oss_write")
            .antMatchers(HttpMethod.DELETE)
              .hasAuthority("SCOPE_oss_write")              
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
