package com.operationapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
public class ApiOperationApplication {

  public static void main(String[] args) {
    SpringApplication.run(ApiOperationApplication.class, args);
  }

}
