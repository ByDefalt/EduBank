package com.example.clientAPI.repository;

import com.example.clientAPI.entity.BankAccountParameterEntity;
import dto.bankapi.BankAccountParameter;
import dto.bankapi.State;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@ActiveProfiles("test")
@Import(BankAccountParameterRepository.class)
class BankAccountParameterRepositoryTest {

    @Autowired
    private BankAccountParameterRepository bankAccountParameterRepository;

    private BankAccountParameterEntity buildParameter(Double overdraftLimit, State state) {
        BankAccountParameterEntity param = new BankAccountParameterEntity();
        param.setOverdraftLimit(overdraftLimit);
        param.setState(state);
        return param;
    }

    // ==================== CREATE ====================

    @Test
    void testCreateParameter() {
        BankAccountParameterEntity param = buildParameter(500.00, State.ACTIVE);

        BankAccountParameterEntity created = bankAccountParameterRepository.createParameter(param);

        assertNotNull(created);
        assertNotNull(created.getId());
        assertTrue(created.getId() > 0);
        assertEquals(500.00, created.getOverdraftLimit());
        assertEquals(State.ACTIVE, created.getState());
    }

    @Test
    void testCreateParameterWithZeroOverdraft() {
        BankAccountParameterEntity param = buildParameter(0.00, State.INACTIVE);

        BankAccountParameterEntity created = bankAccountParameterRepository.createParameter(param);

        assertNotNull(created);
        assertEquals(0.00, created.getOverdraftLimit());
        assertEquals(State.INACTIVE, created.getState());
    }

    // ==================== READ ====================

    @Test
    void testGetAllParameters() {
        bankAccountParameterRepository.createParameter(buildParameter(100.00, State.ACTIVE));
        bankAccountParameterRepository.createParameter(buildParameter(200.00, State.INACTIVE));

        List<BankAccountParameterEntity> result = bankAccountParameterRepository.getAllParameters();

        assertNotNull(result);
        assertTrue(result.size() >= 2);
    }

    // ==================== UPDATE ====================

    @Test
    void testUpdateState() {
        BankAccountParameterEntity created = bankAccountParameterRepository.createParameter(
                buildParameter(300.00, State.ACTIVE));
        int id = created.getId();

        bankAccountParameterRepository.updateState(id, State.INACTIVE.toString());

        List<BankAccountParameterEntity> all = bankAccountParameterRepository.getAllParameters();
        BankAccountParameterEntity updated = all.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);

        assertNotNull(updated);
        assertEquals(State.INACTIVE, updated.getState());
    }

    @Test
    void testUpdateStateToBloqued() {
        BankAccountParameterEntity created = bankAccountParameterRepository.createParameter(
                buildParameter(400.00, State.ACTIVE));
        int id = created.getId();

        bankAccountParameterRepository.updateState(id, State.BLOQUED.toString());

        List<BankAccountParameterEntity> all = bankAccountParameterRepository.getAllParameters();
        BankAccountParameterEntity updated = all.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);

        assertNotNull(updated);
        assertEquals(State.BLOQUED, updated.getState());
    }

    @Test
    void testUpdateOverdraftLimit() {
        BankAccountParameterEntity created = bankAccountParameterRepository.createParameter(
                buildParameter(100.00, State.ACTIVE));
        int id = created.getId();

        bankAccountParameterRepository.updateOverdraftLimit(id, 2000.00);

        List<BankAccountParameterEntity> all = bankAccountParameterRepository.getAllParameters();
        BankAccountParameterEntity updated = all.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);

        assertNotNull(updated);
        assertEquals(2000.00, updated.getOverdraftLimit());
    }

    // ==================== DELETE ====================

    @Test
    void testDeleteParameter() {
        BankAccountParameterEntity created = bankAccountParameterRepository.createParameter(
                buildParameter(500.00, State.ACTIVE));
        int id = created.getId();

        bankAccountParameterRepository.deleteParameter(id);

        List<BankAccountParameterEntity> all = bankAccountParameterRepository.getAllParameters();
        boolean exists = all.stream().anyMatch(p -> p.getId() == id);

        assertFalse(exists);
    }
}
