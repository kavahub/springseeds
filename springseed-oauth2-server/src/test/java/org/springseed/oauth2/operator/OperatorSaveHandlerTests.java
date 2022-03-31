package org.springseed.oauth2.operator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springseed.core.util.CommonRuntimeException;

@SpringBootTest
public class OperatorSaveHandlerTests {
    @Autowired
    private OperatorSaveHandler operatorSaveHandler;

    @Test
    public void giveObject_whenSave_thenOk(){
        Operator saved = operatorSaveHandler.save(new Operator("username1"));
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    public void giveWrongEmail_whenSave_thenException() {
        Operator operator =new Operator("username2");
        operator.setEmail("1@11");

        assertThrows(CommonRuntimeException.class, () -> {
            operatorSaveHandler.save(operator);
        });
    }

    @Test
    public void giveWrongPhoneNumber_whenSave_thenException() {
        Operator operator =new Operator("username3");
        operator.setPhoneNumber("1234567");

        assertThrows(CommonRuntimeException.class, () -> {
            operatorSaveHandler.save(operator);
        });
    }
}
