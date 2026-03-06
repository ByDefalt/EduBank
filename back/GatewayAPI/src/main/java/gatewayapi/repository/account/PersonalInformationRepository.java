package gatewayapi.repository.account;

import dto.accountapi.PersonalInformation;
import gatewayapi.client.AccountClient;
import gatewayapi.wrapper.FeignExecutor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersonalInformationRepository {

    private final AccountClient accountClient;
    private final FeignExecutor feignExecutor;

    public PersonalInformationRepository(AccountClient accountClient, FeignExecutor feignExecutor) {
        this.accountClient = accountClient;
        this.feignExecutor = feignExecutor;
    }

    public List<PersonalInformation> findAll() {
        return feignExecutor.execute(() -> accountClient.getAllPersonalInformation());
    }

    public PersonalInformation findById(Integer id) {
        return feignExecutor.execute(() -> accountClient.getPersonalInformationById(id));
    }
}

