package org.springseed.oauth2.operator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.persistence.PersistenceException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
public class OperatorRepositoryTests {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OperatorRepository operatorRepository;

    @Test
    public void whenFindByName_thenReturnOperator() {
        Operator alex = new Operator("alex");
        entityManager.persistAndFlush(alex);

        Operator found = operatorRepository.findByUsernameIgnoreCase(alex.getUsername());
        assertThat(found.getUsername()).isEqualTo("ALEX");
    }

    @Test
    public void whenInvalidUsername_thenReturnNull() {
        Operator notFound = operatorRepository.findByUsernameIgnoreCase("doesNotExist");
        assertThat(notFound).isNull();
    }

    @Test
    public void givenSameUsername_whenSave_thenPersistenceException() {
        Operator alex1 = new Operator("alex");
        operatorRepository.save(alex1);

        Operator alex2 = new Operator("alex");
        operatorRepository.save(alex2);
        assertThrows(PersistenceException.class, () -> entityManager.flush());
    }

    @Test
    public void givenEmptyOperator_whenSave_thenDataIntegrityViolationException() {
        Operator alex = new Operator();
        assertThrows(DataIntegrityViolationException.class, () -> operatorRepository.save(alex));
    }
}
