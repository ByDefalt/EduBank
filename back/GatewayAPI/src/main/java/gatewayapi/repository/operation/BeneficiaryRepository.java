package gatewayapi.repository.operation;

import dto.operationapi.Beneficiary;
import dto.operationapi.BeneficiaryList;
import gatewayapi.client.OperationClient;
import org.springframework.stereotype.Service;

@Service
public class BeneficiaryRepository {

    private final OperationClient operationClient;

    public BeneficiaryRepository(OperationClient operationClient) {
        this.operationClient = operationClient;
    }

    public BeneficiaryList getAllBeneficiaries() {
        return this.operationClient.getAllBeneficiaries();
    }

    public BeneficiaryList getBeneficiariesByAccountId(String accountId) {
        return this.operationClient.getBeneficiariesByAccountId(accountId);
    }

    public Beneficiary createBeneficiary(Beneficiary beneficiary) {
        return this.operationClient.createBeneficiary(beneficiary);
    }

    public Beneficiary updateBeneficiary(Integer id, Beneficiary beneficiary) {
        return this.operationClient.updateBeneficiary(id, beneficiary);
    }

    public void deleteBeneficiary(Integer id) {
        this.operationClient.deleteBeneficiary(id);
    }
}
