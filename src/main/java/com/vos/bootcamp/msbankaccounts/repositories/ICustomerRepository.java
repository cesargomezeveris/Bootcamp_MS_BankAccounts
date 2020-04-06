package com.vos.bootcamp.msbankaccounts.repositories;

import com.vos.bootcamp.msbankaccounts.models.CustomerType;
import reactor.core.publisher.Mono;

public interface ICustomerRepository {

  public Mono<Boolean> existsCustomer(String numDocCustomer);

  public Mono<CustomerType> getCustomerType(String numDocCustomer);

}
