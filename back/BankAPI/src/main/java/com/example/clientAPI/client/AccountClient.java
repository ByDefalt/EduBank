package com.example.clientAPI.client;

import dto.accountapi.TokenRequest;
import dto.accountapi.TokenResponse;
import feign.Headers;
import feign.RequestLine;

public interface AccountClient {

    @RequestLine("POST /account/validate")
    @Headers("Content-Type: application/json")
    TokenResponse validateToken(TokenRequest tokenRequest);
}
