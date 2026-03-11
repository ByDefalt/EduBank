package gatewayapi.repository.bankaccount;

import dto.bankapiswagger.*;
import gatewayapi.client.BankAccountClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TypeRepository {

    private final BankAccountClient bankAccountClient;

    public TypeRepository(BankAccountClient bankAccountClient) {
        this.bankAccountClient = bankAccountClient;
    }

    public List<Type> getAllTypes() {
        return bankAccountClient.getAllTypes();
    }

    public Type getTypeById(Integer id) {
        return bankAccountClient.getTypeById(id);
    }

    public Type createType(Type type) {
        return bankAccountClient.createType(type);
    }
}