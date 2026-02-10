package com.example.clientAPI.configuration;

import com.example.clientAPI.client.AccountClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.Logger;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import jakarta.inject.Inject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

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
    public AccountClient getAccountClient() {
        return Feign.builder()
                .encoder(new JacksonEncoder(objectMapper))
                .decoder(new JacksonDecoder(objectMapper))
                .client(new OkHttpClient(getOkHttpClient()))
                .logger(new Logger.JavaLogger(FeignConfig.class))
                .logLevel(Logger.Level.FULL)
                .target(AccountClient.class, "http://localhost:8081/api/v1");
    }
}
