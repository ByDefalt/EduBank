package com.operationapi.business;

import com.operationapi.entity.OperationEntity;
import com.operationapi.entity.StateEnumEntity;
import com.operationapi.exception.FunctionalException;
import com.operationapi.mapper.OperationMapper;
import com.operationapi.repository.OperationRepository;
import com.operationapi.util.DateUtils;
import dto.operationapi.Operation;
import dto.operationapi.OperationList;
import dto.operationapi.OperationState;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class OperationBusiness {
    private final OperationRepository operationRepository;

    public OperationBusiness(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    public OperationList getOperations(OperationState state, String dateFrom, String dateTo) {
        List<OperationEntity> operations = this.operationRepository.getOperations(null, state, DateUtils.parseDate(dateFrom), DateUtils.parseDate(dateTo));
        OperationList operationList = new OperationList();
        operationList.setData(OperationMapper.toDto(operations));
        return operationList;
    }

    public OperationList getOperationsByAccountId(String accountId, OperationState state, String dateFrom, String dateTo) {
        List<OperationEntity> operations = this.operationRepository.getOperations(accountId, state, DateUtils.parseDate(dateFrom), DateUtils.parseDate(dateTo));
        OperationList operationList = new OperationList();
        operationList.setData(OperationMapper.toDto(operations));
        return operationList;
    }

    public Operation save(Operation operation) {
        if (operation.getAccountSourceId() == null || operation.getAccountSourceId().isBlank()) {
            throw new FunctionalException("400", "Le champ 'account_source_id' est obligatoire");
        }
        if (operation.getLabel() == null || operation.getLabel().isBlank()) {
            throw new FunctionalException("400", "Le champ 'label' est obligatoire");
        }
        if (operation.getIbanTarget() == null || operation.getIbanTarget().isBlank()) {
            throw new FunctionalException("400", "Le champ 'iban_target' est obligatoire");
        }
        if (operation.getAmount() == null || operation.getAmount() < 0.01) {
            throw new FunctionalException("400", "Le champ 'amount' doit être supérieur à 0");
        }

        operation.setState(OperationState.PENDING);
        if (operation.getDate() == null) {
            operation.setDate(OffsetDateTime.now(ZoneOffset.UTC));
        }

        OperationEntity operationEntity = this.operationRepository.save(OperationMapper.toEntity(operation));
        return OperationMapper.toDto(operationEntity);
    }

    public Operation getOperationById(Integer id) {
        OperationEntity operationEntity = this.operationRepository.getOperationById(id);
        return OperationMapper.toDto(operationEntity);
    }

    public Operation updateStateOperation(Integer id, OperationState state) {
        if (state == null) {
            throw new FunctionalException("400", "Le champ 'state' est obligatoire");
        }
        OperationEntity existing = this.operationRepository.getOperationById(id);
        if (StateEnumEntity.CANCELLED.equals(existing.state())) {
            throw new FunctionalException("400", "Impossible de modifier l'état d'une opération annulée");
        }
        this.operationRepository.updateState(id, state);
        OperationEntity updatedStateOperation = this.operationRepository.getOperationById(id);
        return OperationMapper.toDto(updatedStateOperation);
    }

    public Operation cancelOperation(Integer id) {
        OperationEntity original = this.operationRepository.getOperationById(id);

        if (StateEnumEntity.CANCELLED.equals(original.state())) {
            throw new FunctionalException("400", "L'opération est déjà annulée");
        }

        this.operationRepository.updateState(id, OperationState.CANCELLED);

        OperationEntity cancellation = new OperationEntity(
                null,
                original.accountSourceId(),
                "ANNULATION - " + original.label(),
                StateEnumEntity.COMPLETED,
                original.ibanTarget(),
                -original.amount(),
                LocalDateTime.now()
        );
        OperationEntity savedCancellation = this.operationRepository.save(cancellation);

        return OperationMapper.toDto(savedCancellation);
    }
}