package accountapi.mapper;

import accountapi.entity.AccountEntity;
import dto.accountapi.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public static Account toDto(AccountEntity entity) {
        if (entity == null) {
            return null;
        }

        Account dto = new Account();
        dto.setId(entity.getId());
        dto.setPersonalInfoId(entity.getPersonalInfoId());
        dto.setRoleId(entity.getRoleId());
        dto.setState(entity.getState());

        return dto;
    }

    public static AccountEntity toEntity(Account dto) {
        if (dto == null) {
            return null;
        }

        AccountEntity entity = new AccountEntity();
        entity.setId(dto.getId());
        entity.setPersonalInfoId(dto.getPersonalInfoId());
        entity.setRoleId(dto.getRoleId());
        entity.setState(dto.getState());

        return entity;
    }
}