package gatewayapi.repository.account;

import dto.accountapi.PersonalInformation;
import dto.accountapi.PersonalInformationRegister;
import gatewayapi.client.AccountClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersonalInformationRepository {

    private final AccountClient accountClient;

    public PersonalInformationRepository(AccountClient accountClient) {
        this.accountClient = accountClient;
    }

    public List<PersonalInformation> findAll() {
        return  accountClient.getAllPersonalInformation();
    }

    public PersonalInformation findById(Integer id) {
        return  accountClient.getPersonalInformationById(id);
    }

    public PersonalInformation create(PersonalInformationRegister personalInformationRegister) {
        return  accountClient.createPersonalInformation(personalInformationRegister);
    }

    public PersonalInformation update(Integer id, PersonalInformation personalInformation) {
        return accountClient.updatePersonalInformation(id, personalInformation);
    }
}
