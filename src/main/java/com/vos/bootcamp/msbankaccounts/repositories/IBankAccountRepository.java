package com.vos.bootcamp.msbankaccounts.repositories;

import com.vos.bootcamp.msbankaccounts.models.BankAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface IBankAccountRepository extends ReactiveMongoRepository<BankAccount, String> {

  public Flux<BankAccount> findByNumIdentityDocCustomer(String numDoc);

}
