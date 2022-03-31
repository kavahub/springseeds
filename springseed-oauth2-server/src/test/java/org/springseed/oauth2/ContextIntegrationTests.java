package org.springseed.oauth2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { OAuth2Application.class })
public class ContextIntegrationTests {
    @Test
    public void whenLoadApplication_thenSuccess() {

    } 
}
