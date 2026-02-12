package gatewayapi.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.Logger;
import feign.RequestInterceptor;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import gatewayapi.client.AccountClient;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.TimeUnit;

@Configuration
@Profile("TEST")
public class FeignConfig {

    private okhttp3.OkHttpClient getOkHttpClient() {
        var okHttpClient = new okhttp3.OkHttpClient.Builder();
        okHttpClient.connectTimeout(10000, TimeUnit.MILLISECONDS);
        okHttpClient.readTimeout(10000, TimeUnit.MILLISECONDS);
        return okHttpClient.build();
    }

    @Inject
    private ObjectMapper objectMapper;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                // recupe header
                String token = request.getHeader("Authorization");
                if (token != null) {
                    // injecte dans requestTemplate
                    requestTemplate.header("Authorization", token);
                }
            }
        };
    }

    @Bean
    public AccountClient getAccountClient(RequestInterceptor requestInterceptor) {
        return Feign.builder()
                .encoder(new JacksonEncoder(objectMapper))
                .decoder(new JacksonDecoder(objectMapper))
                .requestInterceptor(requestInterceptor)
                .client(new OkHttpClient(getOkHttpClient()))
                .logger(new Logger.JavaLogger(FeignConfig.class))
                .logLevel(Logger.Level.FULL)
                .target(AccountClient.class, "http://localhost:8081/api/v1");
    }
}