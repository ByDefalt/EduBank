package accountapi.interception;

import accountapi.annotation.AuthenticationRequired;
import accountapi.business.AccountBusiness;
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
import java.io.IOException;
import java.lang.reflect.Method;

@Provider
@AuthenticationRequired
public class AuthenticationRequiredImpl implements ContainerRequestFilter {

    private final AccountBusiness accountBusiness;

    @Context
    private ResourceInfo resourceInfo;

    @Autowired
    public AuthenticationRequiredImpl(AccountBusiness accountBusiness) {
        this.accountBusiness = accountBusiness;
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        String authHeader = containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            containerRequestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Authentification requise").build());
            return;
        }
        String token = authHeader.substring("Bearer ".length()).trim();

        try {
            Method method = resourceInfo.getResourceMethod();
            AuthenticationRequired annotation = method.getAnnotation(AuthenticationRequired.class);

            if (annotation != null) {
                RoleEnum requiredRole = annotation.value();

                TokenRequest tokenRequest = new TokenRequest();
                tokenRequest.setJwt(token);
                TokenResponse tokenResponse = accountBusiness.validateToken(tokenRequest);
                if (tokenResponse == null) {
                    containerRequestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("token expiree ou invalide").build());
                    return;
                } else {
                    if (requiredRole.toString().equals(tokenResponse.getRole())) {
                        // autorisee
                        return;
                    } else {
                        containerRequestContext.abortWith(Response.status(Response.Status.FORBIDDEN).entity("permission insuffisante").build());
                        return;
                    }
                }

            }else {
                containerRequestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("token expiree ou invalide").build());
            }
        } catch (Exception e) {
            containerRequestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("token expiree ou invalide").build());
        }
    }
}
