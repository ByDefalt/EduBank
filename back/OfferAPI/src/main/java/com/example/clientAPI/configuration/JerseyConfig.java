package com.example.clientAPI.configuration;

import com.example.clientAPI.controller.OfferController;
import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
@ApplicationPath("/api/v1")
public class JerseyConfig extends ResourceConfig {

  public JerseyConfig() {
    register(OfferController.class);
  }
}
