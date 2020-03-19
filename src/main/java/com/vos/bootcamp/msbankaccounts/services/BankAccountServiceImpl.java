package com.vos.bootcamp.msbankaccounts.services;

import com.vos.bootcamp.msbankaccounts.models.BankAccount;
import com.vos.bootcamp.msbankaccounts.repositories.IBankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class BankAccountServiceImpl implements IBankAccountService {

  @Autowired
  private IBankAccountRepository repository;

  @Override
  public Flux<BankAccount> findAll() {
    return repository.findAll();
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
