package com.vos.bootcamp.msbankaccounts.services;

import com.vos.bootcamp.msbankaccounts.models.BankAccountType;
import com.vos.bootcamp.msbankaccounts.repositories.IBankAccountTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BankAccountTypeServiceImpl implements IBankAccountTypeService {

  @Autowired
  private IBankAccountTypeRepository repository;

  @Override
  public Flux<BankAccountType> findAll() {
    return repository.findAll();
  }

  @Override
  public Mono<BankAccountType> findById(String id) {
    return repository.findById(id);
  }

  @Override
  public Mono<BankAccountType> save(BankAccountType bankAccountType) {
    return repository.save(bankAccountType);
  }

  @Override
  public Mono<BankAccountType> update(String id, BankAccountType bankAccountType) {
    return repository.findById(id)
            .flatMap(bankAccountTypeDB -> {

              if (bankAccountType.getName() == null) {
                bankAccountTypeDB.setName(bankAccountTypeDB.getName());
              } else {
                bankAccountTypeDB.setName(bankAccountType.getName());
              }

              return repository.save(bankAccountTypeDB);

            });
  }

  @Override
  public Mono<Void> delete(BankAccountType bankAccountType) {
    return repository.delete(bankAccountType);
  }

  @Override
  public Mono<Void> deleteById(String id) {
    return repository.findById(id)
            .flatMap(this::delete);
  }
}
