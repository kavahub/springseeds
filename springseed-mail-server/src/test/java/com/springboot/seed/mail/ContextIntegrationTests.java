package com.springboot.seed.mail;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { MailApplication.class })
public class ContextIntegrationTests {
    @Autowired
    private JavaMailSender emailSender;
    
    @Test
    public void whenLoadApplication_thenSuccess() {
        assertThat(emailSender).isNotNull();
    } 
}
