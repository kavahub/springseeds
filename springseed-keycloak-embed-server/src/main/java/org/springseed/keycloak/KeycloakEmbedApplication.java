package org.springseed.keycloak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;

/**
 * 程序入口
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@SpringBootApplication(exclude = LiquibaseAutoConfiguration.class)
public class KeycloakEmbedApplication {
    public static void main(String[] args) throws Exception {
		SpringApplication.run(KeycloakEmbedApplication.class, args);
	}
}
