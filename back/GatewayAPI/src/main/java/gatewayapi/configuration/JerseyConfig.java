package gatewayapi.configuration;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
@ApplicationPath("/api/v1")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        packages("gatewayapi.controller");
        packages("gatewayapi.client");
        packages("gatewayapi.wrapper");
        packages("gatewayapi.filter");
    }
}
