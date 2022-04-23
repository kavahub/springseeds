package org.springseed.keycloak;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { KeycloakEmbedApplication.class })
@SpringseedActiveProfiles
public class ContextIntegrationTests {
    @Test
    public void whenLoadApplication_thenSuccess() {

    } 
}
