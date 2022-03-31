package org.springseed.oauth2.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.builders.JdbcClientDetailsServiceBuilder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 令牌存储方式配置，Jwt和Jdbc(数据库)两种方式
 *
 * @author PinWei Wan
 * @since 1.0.0
 */
@Slf4j
public class TokenStoreAutoConfig {

    @Configuration
    @ConditionalOnProperty(name = "springbootseeds.oauth2.tokenstore", havingValue = "jwt", matchIfMissing = true)
    public static class JwtTokenStoreConfig {
        @Autowired
        private BCryptPasswordEncoder passwordEncoder;

        @Bean
        public InMemoryClientDetailsServiceBuilder clientDetailsServiceBuilder() {
            final InMemoryClientDetailsServiceBuilder builder = new InMemoryClientDetailsServiceBuilder();
            // 配置客户端
            builder.withClient("fooClient")
                    .secret(passwordEncoder.encode("secret"))
                    .authorizedGrantTypes("password", "authorization_code", "refresh_token", "client_credentials")
                    .scopes("foo,read,write")
                    .accessTokenValiditySeconds(3600) // 1 hour
                    .refreshTokenValiditySeconds(2592000) // 30 days
                    .autoApprove(true)
                    .and()
                    .withClient("sampleClientId")
                    .secret(passwordEncoder.encode("secret"))
                    .authorizedGrantTypes("implicit")
                    .scopes("read,write,foo,bar")
                    .accessTokenValiditySeconds(3600) // 1 hour
                    .refreshTokenValiditySeconds(2592000) // 30 days
                    .autoApprove(false)
                    .and()
                    .withClient("barClient")
                    .secret(passwordEncoder.encode("secret"))
                    .authorizedGrantTypes("password", "authorization_code", "refresh_token")
                    .scopes("bar,read,write")
                    .accessTokenValiditySeconds(3600) // 1 hour
                    .refreshTokenValiditySeconds(2592000) // 30 days
                    .autoApprove(true);
            return builder;
        }

        @Bean
        public TokenStore tokenStore(final JwtAccessTokenConverter convert) {
            log.info("JwtTokenStore is used");
            return new JwtTokenStore(convert);
        }
    }

    @Configuration
    @ConditionalOnProperty(name = "springbootseeds.oauth2.tokenstore", havingValue = "jdbc")
    public static class JdbcTokenStoreConfig{
        @Autowired
        private BCryptPasswordEncoder passwordEncoder;
        @Autowired
        private DataSource dataSource;

        @Bean
        public JdbcClientDetailsServiceBuilder clientDetailsServiceBuilder() {
            final JdbcClientDetailsServiceBuilder builder = new JdbcClientDetailsServiceBuilder();
            builder.dataSource(dataSource).passwordEncoder(passwordEncoder);
            return builder;
        }

        @Bean
        public TokenStore tokenStore() {
            log.info("JdbcTokenStore is used");
            return new JdbcTokenStore(dataSource);
        }

        @Bean
        public DataSourceInitializer dataSourceInitializer() {
            final DataSourceInitializer initializer = new DataSourceInitializer();
            initializer.setDataSource(dataSource);
            initializer.setDatabasePopulator(databasePopulator());
            return initializer;
        }

        private DatabasePopulator databasePopulator() {
            final Resource schemaScript = new ClassPathResource("tokenstore.h2.sql");
            final Resource dataScript = new ClassPathResource("data.h2.sql");
            final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            populator.addScript(schemaScript);
            populator.addScript(dataScript);
            return populator;
        }
    }
}
