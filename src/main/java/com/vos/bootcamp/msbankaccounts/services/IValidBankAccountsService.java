package com.vos.bootcamp.msbankaccounts.services;

import com.vos.bootcamp.msbankaccounts.models.CustomerType;
import reactor.core.publisher.Mono;

public interface IValidBankAccountsService {

  public Mono<Boolean> existsCustomer(String id);

  public Mono<CustomerType> getCustomerType(String id);

  public Mono<Number> getCountBankAccounts(String numIdeDoc);

  public Mono<Number> getCountBankAccountsByType(String numIdeDoc, String type);

  public Mono<Boolean> validateRegisterCustomer(String numDoc, String type);


}
