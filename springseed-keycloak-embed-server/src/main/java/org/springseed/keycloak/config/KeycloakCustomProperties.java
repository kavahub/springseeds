package org.springseed.keycloak.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import lombok.Data;

/**
 * 属性
 *  
 * @author PinWei Wan
 * @since 1.0.0
 */
@Data
@ConfigurationProperties(prefix = "keycloak.custom")
public class KeycloakCustomProperties {
  
    private Server server = new Server();

    private AdminUser adminUser = new AdminUser();

    private Migration migration = new Migration();

    private Infinispan infinispan = new Infinispan();

    @Data
    public static class Server {

        /**
         * Path relative to {@code server.servlet.context-path} for the Keycloak JAX-RS Application to listen to.
         */
        private String contextPath = "/auth";
    }

    @Data
    public static class Migration {
        private Resource importLocation = new ClassPathResource("keycloak-realm-config.json");

        private String importProvider = "singleFile";

        private boolean importEnabled = false;
    }

    @Data
    public static class Infinispan {

        private Resource configLocation = new ClassPathResource("infinispan.xml");
    }

    @Data
    public static class AdminUser {

        private boolean createAdminUserEnabled = true;

        private String username = "admin";

        private String password;
    }  
}
