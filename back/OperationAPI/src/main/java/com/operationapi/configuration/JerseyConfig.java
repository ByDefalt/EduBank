package com.operationapi.configuration;

import com.operationapi.filters.AuthenticationFilter;
import com.operationapi.handlers.FunctionalExceptionHandler;
import com.operationapi.handlers.NotFoundExceptionHandler;
import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
@ApplicationPath("/api/v1")
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        packages("com.operationapi.controller");
        register(AuthenticationFilter.class);
        register(FunctionalExceptionHandler.class);
        register(NotFoundExceptionHandler.class);
    }
}
