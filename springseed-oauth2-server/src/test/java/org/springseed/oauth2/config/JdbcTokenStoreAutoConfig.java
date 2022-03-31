package org.springseed.oauth2.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.builders.JdbcClientDetailsServiceBuilder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = { "springbootseeds.oauth2.tokenstore = jdbc" })
public class JdbcTokenStoreAutoConfig {
    @Autowired
    private ClientDetailsServiceBuilder<?> ClientDetailsServiceBuilder;
    @Autowired
    private TokenStore tokenStore;

    @Test
    public void isJwtTokenStore() {
        assertThat(tokenStore).isNotNull();
        assertThat(tokenStore).isExactlyInstanceOf(JdbcTokenStore.class);
    }
    
    @Test
    public void isInMemoryClientDetailsServiceBuilder() {
        assertThat(ClientDetailsServiceBuilder).isNotNull();
        assertThat(ClientDetailsServiceBuilder).isExactlyInstanceOf(JdbcClientDetailsServiceBuilder.class);
    }
}
