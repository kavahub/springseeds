package org.springseed.keycloak.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import lombok.Data;

/**
 * TODO
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@Data
@ConfigurationProperties(prefix = "keycloak.custom")
public class KeycloakCustomProperties {
  
    Server server = new Server();

    AdminUser adminUser = new AdminUser();

    Migration migration = new Migration();

    Infinispan infinispan = new Infinispan();

    @Data
    public static class Server {

        /**
         * Path relative to {@code server.servlet.context-path} for the Keycloak JAX-RS Application to listen to.
         */
        String contextPath = "/auth";
    }

    @Data
    public static class Migration {

        Resource importLocation = new ClassPathResource("keycloak-realm-config.json");

        String importProvider = "singleFile";
    }

    @Data
    public static class Infinispan {

        Resource configLocation = new ClassPathResource("infinispan.xml");
    }

    @Data
    public static class AdminUser {

        boolean createAdminUserEnabled = true;

        String username = "admin";

        String password;
    }  
}
