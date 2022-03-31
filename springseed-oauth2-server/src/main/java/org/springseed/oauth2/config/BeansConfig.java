package org.springseed.oauth2.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

/**
 * 
 * Bean配置
 *
 * @author PinWei Wan
 * @since 1.0.0
 */
@Configuration
public class BeansConfig {
    /**
     * 实体对象映射工具
     * 
     * @return
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    } 

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        final KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("springseeds.jks"),
                "7WWundC8gVH1Qc1O".toCharArray());
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("springseeds"));
        return converter;
    }

    /**
     * 自定义缓存过期时间
     * @return
     */
    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return (builder) -> {
            Map<String, RedisCacheConfiguration> configurationMap = new HashMap<>();
            configurationMap.put("entitycache-operators", RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(1)));   
            builder.withInitialCacheConfigurations(configurationMap);
        };
    }
}
