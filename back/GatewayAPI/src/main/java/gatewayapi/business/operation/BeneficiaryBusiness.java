package gatewayapi.business.operation;

import dto.operationapi.Beneficiary;
import dto.operationapi.BeneficiaryList;
import gatewayapi.repository.operation.BeneficiaryRepository;
import org.springframework.stereotype.Service;

@Service
public class BeneficiaryBusiness {

    private final BeneficiaryRepository beneficiaryRepository;

    public BeneficiaryBusiness(BeneficiaryRepository beneficiaryRepository) {
        this.beneficiaryRepository = beneficiaryRepository;
    }

    public BeneficiaryList getAllBeneficiaries() {
        return this.beneficiaryRepository.getAllBeneficiaries();
    }

    public BeneficiaryList getBeneficiariesByAccountId(String accountId) {
        return this.beneficiaryRepository.getBeneficiariesByAccountId(accountId);
    }

    public Beneficiary createBeneficiary(Beneficiary beneficiary) {
        return this.beneficiaryRepository.createBeneficiary(beneficiary);
    }

    public Beneficiary updateBeneficiary(Integer id, Beneficiary beneficiary) {
        return this.beneficiaryRepository.updateBeneficiary(id, beneficiary);
    }

    public void deleteBeneficiary(Integer id) {
        this.beneficiaryRepository.deleteBeneficiary(id);
    }
}
