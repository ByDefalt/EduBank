package gatewayapi.wrapper;

import feign.FeignException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Component;

@Component
public class FeignExecutor {

    public <T> Response wrap(FeignCall<T> call) {
        try {
            T result = call.execute();

            if (result == null) {
                return Response.noContent().build();
            }
            return Response.ok(result).build();

        } catch (FeignException e) {
            int status = e.status();

            String errorMessage = e.contentUTF8();

            if (errorMessage != null && !errorMessage.isEmpty()) {
                return Response.status(status).entity(errorMessage).type(MediaType.TEXT_PLAIN).build();
            } else {
                return Response.status(status).build();
            }
        }
    }
}