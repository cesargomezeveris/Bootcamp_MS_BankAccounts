package com.vos.bootcamp.msbankaccounts.repositories;

import com.vos.bootcamp.msbankaccounts.models.CustomerType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
public class CustomerRepositoryImpl implements ICustomerRepository {

  private WebClient webClient = WebClient.create("http://localhost:8001/api/customers");

  @Override
  public Mono<Boolean> existsCustomer(String numDocCustomer) {
    return webClient
            .get()
            .uri("/" + numDocCustomer + "/exist")
            .retrieve()
            .bodyToMono(Boolean.class);

  }

  @Override
  public Mono<CustomerType> getCustomerType(String numDocCustomer) {
    return webClient
            .get()
            .uri("/" + numDocCustomer + "/customerType")
            .retrieve()
            .bodyToMono(CustomerType.class);
  }

}
