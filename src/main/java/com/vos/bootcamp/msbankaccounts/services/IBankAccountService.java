package com.vos.bootcamp.msbankaccounts.services;

import com.vos.bootcamp.msbankaccounts.models.BankAccount;
import com.vos.bootcamp.msbankaccounts.models.CustomerType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IBankAccountService {

  public Flux<BankAccount> findAll();

  public Flux<BankAccount> findAllByNumIdeDoc(String numIdeDoc);

  public Mono<Boolean> existsCustomer(String id);

  public Mono<CustomerType> getCustomerType(String id);

  public Mono<Number> getCountBankAccounts(String numIdeDoc);

  public Mono<Number> getCountBankAccountsByType(String numIdeDoc, String type);

  public Mono<BankAccount> findById(String id);

  public Mono<BankAccount> save(BankAccount bankAccount);

  public Mono<BankAccount> update(String id, BankAccount bankAccount);

  public Mono<Void> delete(BankAccount customer);

  public Mono<Void> deleteById(String id);

}
