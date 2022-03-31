package org.springseed.oauth2.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@SpringBootTest
public class JwtTokenStoreAutoConfig {
    @Autowired
    private ClientDetailsServiceBuilder<?> ClientDetailsServiceBuilder;
    @Autowired
    private TokenStore tokenStore;

    @Test
    public void isJwtTokenStore() {
        assertThat(tokenStore).isNotNull();
        assertThat(tokenStore).isExactlyInstanceOf(JwtTokenStore.class);
    }
    
    @Test
    public void isInMemoryClientDetailsServiceBuilder() {
        assertThat(ClientDetailsServiceBuilder).isNotNull();
        assertThat(ClientDetailsServiceBuilder).isExactlyInstanceOf(InMemoryClientDetailsServiceBuilder.class);
    }
}
