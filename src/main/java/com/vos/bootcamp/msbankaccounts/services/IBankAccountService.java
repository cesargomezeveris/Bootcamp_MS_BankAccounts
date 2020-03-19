package com.vos.bootcamp.msbankaccounts.services;

import com.vos.bootcamp.msbankaccounts.models.BankAccount;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IBankAccountService {

  public Flux<BankAccount> findAll();

  public Mono<BankAccount> findById(String id);

  public Mono<BankAccount> save(BankAccount bankAccount);

  public Mono<BankAccount> update(String id, BankAccount bankAccount);

  public Mono<Void> delete(BankAccount customer);

  public Mono<Void> deleteById(String id);

}
