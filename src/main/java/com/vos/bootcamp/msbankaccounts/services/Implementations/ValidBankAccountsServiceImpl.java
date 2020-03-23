package com.vos.bootcamp.msbankaccounts.services.Implementations;

import com.vos.bootcamp.msbankaccounts.models.CustomerType;
import com.vos.bootcamp.msbankaccounts.repositories.IBankAccountRepository;
import com.vos.bootcamp.msbankaccounts.services.IValidBankAccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class ValidBankAccountsServiceImpl implements IValidBankAccountsService {

  @Autowired
  private IBankAccountRepository repository;

  @Override
  public Mono<Boolean> existsCustomer(String id) {
    return WebClient.create()
            .get()
            .uri("http://localhost:8001/api/customers/" + id + "/exist")
            .retrieve()
            .bodyToMono(Boolean.class);

  }

  @Override
  public Mono<CustomerType> getCustomerType(String id) {
    return WebClient.create()
            .get()
            .uri("http://localhost:8001/api/customers/" + id + "/exist")
            .retrieve()
            .bodyToMono(CustomerType.class);
  }

  @Override
  public Mono<Number> getCountBankAccounts(String numIdeDoc) {
    return Mono.from(repository.findByNumIdentityDocCustomer(numIdeDoc).count());
  }


  @Override
  public Mono<Number> getCountBankAccountsByType(String numIdeDoc, String type) {
    return Mono.from(repository.findByNumIdentityDocCustomer(numIdeDoc)
            .filter(bankAccount -> bankAccount.getBankAccountType().getName().equals(type))
            .count());
  }
}
