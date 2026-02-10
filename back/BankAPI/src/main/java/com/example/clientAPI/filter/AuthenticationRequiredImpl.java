package com.example.clientAPI.filter;

import com.example.clientAPI.annotation.AuthenticationRequired;
import com.example.clientAPI.client.AccountClient;
import dto.accountapi.RoleEnum;
import dto.accountapi.TokenRequest;
import dto.accountapi.TokenResponse;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import java.lang.reflect.Method;

@Provider
@AuthenticationRequired
public class AuthenticationRequiredImpl implements ContainerRequestFilter {

    private final AccountClient accountClient;

    @Context
    private ResourceInfo resourceInfo;


    public AuthenticationRequiredImpl(AccountClient accountClient) {
        this.accountClient = accountClient;
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        String authHeader = containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            containerRequestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Authentification requise").build());
            return;
        }
        String token = authHeader.substring("Bearer ".length()).trim();

        try {
            // recuperee l'annotation sur la methode
            Method method = resourceInfo.getResourceMethod();
            // obtenir le role requis depuis l'annotation appelee
            AuthenticationRequired annotation = method.getAnnotation(AuthenticationRequired.class);

            if (annotation != null) {
                // recuperation du role via la value de l'annotation
                RoleEnum requiredRole = annotation.value();

                TokenRequest tokenRequest = new TokenRequest();
                tokenRequest.setJwt(token);
                TokenResponse tokenResponse = accountClient.validateToken(tokenRequest);
                if (tokenResponse == null) {
                    containerRequestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("token expiree ou invalide").build());
                    return;
                }

                // Stocker l'ID utilisateur et le rôle dans le contexte
                containerRequestContext.setProperty("userId", tokenResponse.getId());
                containerRequestContext.setProperty("userRole", tokenResponse.getRole());

                // si le role correspond au role requis
                if (requiredRole.toString().equals(tokenResponse.getRole())) {
                    return;
                } else if (tokenResponse.getRole().equals(RoleEnum.ADMIN.toString())) {
                    return;
                } else {
                    containerRequestContext.abortWith(Response.status(Response.Status.FORBIDDEN).entity("permission insuffisante").build());
                }
            } else {
                containerRequestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("token expiree ou invalide").build());
            }
        } catch (Exception e) {
            containerRequestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("token expiree ou invalide").build());
        }
    }
}
