package gatewayapi.mapper;

import dto.bankapiswagger.BankAccount;
import dto.bankapiswagger.BankAccountDetail;

public class BankAccountMapper {

    public static BankAccount toBankAccount(BankAccountDetail bankAccountDetail) {
        if(bankAccountDetail == null) {
            return null;
        }
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(bankAccountDetail.getId());
        bankAccount.setParameterId(bankAccountDetail.getParameter().getId());
        bankAccount.setTypeId(bankAccountDetail.getType().getId());
        bankAccount.setIban(bankAccountDetail.getIban());
        bankAccount.setSold(bankAccountDetail.getSold());
        return bankAccount;
    }
}
