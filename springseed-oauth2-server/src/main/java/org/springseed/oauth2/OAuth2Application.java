package org.springseed.oauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 
 * 程序入口
 *
 * @author PinWei Wan
 * @since 1.0.0
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableCaching
public class OAuth2Application {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(OAuth2Application.class, args);
    }
}
