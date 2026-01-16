package com.operationapi.business;


import com.operationapi.entity.AccountEntity;
import com.operationapi.mapper.AccountMapper;
import com.operationapi.repository.AccountRepository;
import com.operationapi.utils.JwtUtils;
import dto.accountApi.Account;
import dto.accountApi.JwtToken;
import org.springframework.stereotype.Service;

@Service
public class AccountBusiness {

    private final AccountRepository accountRepository;

    private final JwtUtils jwtUtils;

    public AccountBusiness(AccountRepository accountRepository, JwtUtils jwtUtils) {
        this.accountRepository = accountRepository;
        this.jwtUtils = jwtUtils;
    }



    public Account createAccount(Account dto) {
        AccountEntity entity = AccountMapper.toEntity(dto);
        accountRepository.createAccount(entity);
        return AccountMapper.toDto(entity);
    }

    public Account getAccountById(String id) {
        AccountEntity entity = accountRepository.getAccountById(id);
        return AccountMapper.toDto(entity);
    }

    public Account updateAccount(String id, Account dto) {
        AccountEntity entity = AccountMapper.toEntity(dto);
        accountRepository.updateAccount(id, entity);
        return AccountMapper.toDto(entity);
    }

    public void deleteAccount(String id) {
        accountRepository.deleteAccount(id);
    }

    public JwtToken signIn(Account dto) {
        AccountEntity entity = accountRepository.getAccountByUsernameAndPassword(dto.getUsername(), dto.getPassword());
        if (entity != null) {
            JwtToken jwtToken = new JwtToken();
            jwtToken.setToken(jwtUtils.generateToken(entity.getEmail()));
            return jwtToken;
        }
        return null;
    }

    public boolean validateToken(String token) {
        String email = jwtUtils.validateToken(token);
        AccountEntity entity = accountRepository.getAccountByEmail(email);
        if (entity != null) {
            return entity.getEmail().equals(email);
        }
        return false;
    }
}
