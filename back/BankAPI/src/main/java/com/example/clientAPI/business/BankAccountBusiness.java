package com.example.clientAPI.business;


import com.example.clientAPI.repository.BankAccountRepository;
import org.springframework.stereotype.Service;

@Service
public class BankAccountBusiness {

    private final BankAccountRepository bankAccountRepository;

    public BankAccountBusiness(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

}
