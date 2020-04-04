package com.vos.bootcamp.msbankaccounts.services.implementations;

import com.vos.bootcamp.msbankaccounts.models.BankAccount;
import com.vos.bootcamp.msbankaccounts.repositories.IBankAccountRepository;
import com.vos.bootcamp.msbankaccounts.services.IBankAccountService;
import com.vos.bootcamp.msbankaccounts.services.IValidateBankAccountsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Slf4j
@Service
public class BankAccountServiceImpl implements IBankAccountService {

  private final IBankAccountRepository repository;
  private final IValidateBankAccountsService validateService;

  public BankAccountServiceImpl(IBankAccountRepository repository,
                                IValidateBankAccountsService validateService) {
    this.repository = repository;
    this.validateService = validateService;
  }

  @Override
  public Flux<BankAccount> findAll() {
    return repository.findAll();
  }

  @Override
  public Flux<BankAccount> findAllByNumIdeDoc(String numIdeDoc) {
    return repository.findByNumIdentityDocCustomer(numIdeDoc);
  }

  @Override
  public Mono<BankAccount> findByAccountNumber(String accountNumber) {
    return repository.findByAccountNumber(accountNumber);
  }

  @Override
  public Mono<BankAccount> findById(String id) {
    return repository.findById(id);
  }


  @Override
  public Mono<Boolean> existsByAccountNumber(String accountNumber) {
    return repository.existsByAccountNumber(accountNumber);
  }

  @Override
  public Mono<BankAccount> save(BankAccount bankAccount) {

    Mono<Boolean> validRes = validateService.validateRegisterCustomer(
            bankAccount.getNumIdentityDocCustomer(),
            bankAccount.getBankAccountType().getName());

    return validRes.flatMap(resValid -> {
      if (resValid) {
        bankAccount.setCreatedAt(new Date());
        return repository.save(bankAccount);
      } else {
        log.error("Ocurrio un problema, revisar las validaciones");
        return Mono.error(new Exception("Ocurrio un problema, revisar las validaciones"));
      }
    });
  }

  @Override
  public Mono<BankAccount> update(String id, BankAccount bankAccount) {
    return repository.findById(id)
            .flatMap(bankAccountDB -> {

              bankAccountDB.setUpdatedAt(new Date());

              if (bankAccount.getAmountAvailable() == null) {
                bankAccountDB.setAmountAvailable(bankAccountDB.getAmountAvailable());
              } else {
                bankAccountDB.setAmountAvailable(bankAccount.getAmountAvailable());
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
