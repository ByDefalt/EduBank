package gatewayapi.repository.bankaccount;

import dto.bankapiswagger.*;
import gatewayapi.client.BankAccountClient;
import org.springframework.stereotype.Component;

@Component
public class BankAccountParameterRepository {

    private final BankAccountClient bankAccountClient;

    public BankAccountParameterRepository(BankAccountClient bankAccountClient) {
        this.bankAccountClient = bankAccountClient;
    }

    // ==================== ADMIN ====================

    public void updateParameters(String bankAccountId, BankAccountParameter parameters) {
        bankAccountClient.updateParameters(bankAccountId, parameters);
    }
}
