package com.vos.bootcamp.msbankaccounts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class MsBankAccountsApplication {

  public static void main(String[] args) {

    SpringApplication.run(MsBankAccountsApplication.class, args);
  }

}
