package com.vos.bootcamp.msbankaccounts.services;

import com.vos.bootcamp.msbankaccounts.models.BankAccount;
import com.vos.bootcamp.msbankaccounts.models.CustomerType;
import com.vos.bootcamp.msbankaccounts.repositories.IBankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BankAccountServiceImpl implements IBankAccountService {

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
    return Mono.from(findAllByNumIdeDoc(numIdeDoc).count());
  }

  @Override
  public Mono<Number> getCountBankAccountsByType(String numIdeDoc, String type) {
    return null;
  }

  @Override
  public Flux<BankAccount> findAll() {
    return repository.findAll();
  }

  @Override
  public Flux<BankAccount> findAllByNumIdeDoc(String numIdeDoc) {
    return repository.findAllByNumIdentityDoc(numIdeDoc);
  }

  @Override
  public Mono<BankAccount> findById(String id) {
    return repository.findById(id);
  }

  @Override
  public Mono<BankAccount> save(BankAccount bankAccount) {
    return repository.save(bankAccount);
  }

  @Override
  public Mono<BankAccount> update(String id, BankAccount bankAccount) {
    return repository.findById(id)
            .flatMap(bankAccountDB -> {

              if (bankAccount.getAccountNumber() == null) {
                bankAccountDB.setAccountNumber(bankAccountDB.getAccountNumber());
              } else {
                bankAccountDB.setAccountNumber(bankAccount.getAccountNumber());
              }

              return repository.save(bankAccountDB);

            });
  }

  @Override
  public Mono<Void> delete(BankAccount customer) {
    return repository.delete(customer);
  }

  @Override
  public Mono<Void> deleteById(String id) {
    return repository.findById(id)
            .flatMap(this::delete);
  }
}
