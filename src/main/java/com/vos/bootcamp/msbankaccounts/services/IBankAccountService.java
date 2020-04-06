package com.vos.bootcamp.msbankaccounts.services;

import com.vos.bootcamp.msbankaccounts.models.BankAccount;
import com.vos.bootcamp.msbankaccounts.util.ICrud;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IBankAccountService extends ICrud<BankAccount> {

  public Flux<BankAccount> findAllByNumIdeDoc(String numIdeDoc);

  public Mono<BankAccount> findByAccountNumber(String accountNumber);

  public Mono<Boolean> existsByAccountNumber(String accountNumber);

  public Mono<Number> getCountBankAccounts(String numIdeDoc);

  public Mono<Number> getCountBankAccountsByType(String numIdeDoc, String type);

  public Mono<Boolean> validateRegisterCustomer(String numDoc, String type);

}
