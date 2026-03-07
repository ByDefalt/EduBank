package com.operationapi.filters;

import com.operationapi.annotation.AuthenticationRequired;
import com.operationapi.clients.AccountClient;
import dto.accountapi.Account;
import dto.accountapi.TokenRequest;
import feign.FeignException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Provider
@Component
@AuthenticationRequired
public class AuthenticationFilter implements ContainerRequestFilter {

    private final AccountClient accountClient;

    public AuthenticationFilter(AccountClient accountClient) {
        this.accountClient = accountClient;
    }

    @Override
    public void filter(ContainerRequestContext ctx) throws IOException {
        String authHeader = ctx.getHeaderString("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            this.abortUnauthorized(ctx, "TOKEN_MISSING", "Token manquant");
            return;
        }

        String token = authHeader.substring(7).trim();

        try {
            TokenRequest tokenRequest = new TokenRequest();
            tokenRequest.setJwt(token);
            Account account = this.accountClient.validateToken(tokenRequest);
            if (account != null) {
                ctx.setProperty("authenticatedAccount", account);
            } else {
                this.abortUnauthorized(ctx, "TOKEN_INVALID", "Token invalide");
            }
        } catch (FeignException e) {
            this.abortUnauthorized(ctx, "TOKEN_INVALID", "Token invalide");
        }
    }

    private void abortUnauthorized(ContainerRequestContext ctx, String code, String message) {
        dto.operationapi.Error error = new dto.operationapi.Error();
        error.setCode(code);
        error.setMessage(message);
        ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build()
        );
    }
}
