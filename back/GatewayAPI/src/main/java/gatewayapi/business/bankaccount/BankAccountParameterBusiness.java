package gatewayapi.business.bankaccount;

import dto.bankapiswagger.*;
import gatewayapi.repository.bankaccount.BankAccountParameterRepository;
import org.springframework.stereotype.Service;

@Service
public class BankAccountParameterBusiness {

    private final BankAccountParameterRepository bankAccountParameterRepository;

    public BankAccountParameterBusiness(BankAccountParameterRepository bankAccountParameterRepository) {
        this.bankAccountParameterRepository = bankAccountParameterRepository;
    }

    // ==================== ADMIN ====================

    public void updateParameters(String bankAccountId, BankAccountParameter parameters) {
        bankAccountParameterRepository.updateParameters(bankAccountId, parameters);
    }
}