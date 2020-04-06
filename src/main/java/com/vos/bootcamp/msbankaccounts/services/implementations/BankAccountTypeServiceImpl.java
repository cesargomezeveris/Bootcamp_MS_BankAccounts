package com.vos.bootcamp.msbankaccounts.services.implementations;

import com.vos.bootcamp.msbankaccounts.models.BankAccountType;
import com.vos.bootcamp.msbankaccounts.repositories.IBankAccountTypeRepository;
import com.vos.bootcamp.msbankaccounts.services.IBankAccountTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BankAccountTypeServiceImpl implements IBankAccountTypeService {

  private final IBankAccountTypeRepository repository;

  public BankAccountTypeServiceImpl(IBankAccountTypeRepository repository) {
    this.repository = repository;
  }

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
  public Mono<BankAccountType> deleteById(String id) {
    return repository.findById(id)
            .flatMap(bankAccountType -> repository.delete(bankAccountType)
                      .then(Mono.just(bankAccountType)));
  }
}
