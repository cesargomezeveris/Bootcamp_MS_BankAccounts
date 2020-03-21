package com.vos.bootcamp.msbankaccounts.services;

import com.vos.bootcamp.msbankaccounts.models.BankAccountType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IBankAccountTypeService {

  public Flux<BankAccountType> findAll();

  public Mono<BankAccountType> findById(String id);

  public Mono<BankAccountType> save(BankAccountType bankAccountType);

  public Mono<BankAccountType> update(String id, BankAccountType bankAccountType);

  public Mono<Void> delete(BankAccountType bankAccountType);

  public Mono<Void> deleteById(String id);

}
