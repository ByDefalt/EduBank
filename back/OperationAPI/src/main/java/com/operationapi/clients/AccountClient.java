package com.operationapi.clients;

import dto.accountapi.Account;
import dto.accountapi.TokenRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "account-api", url = "${accountapi.url}")
public interface AccountClient {

    @PostMapping("/accounts/validate")
    Account validateToken(@RequestBody TokenRequest tokenRequest);
}

