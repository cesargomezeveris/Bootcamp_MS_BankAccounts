package com.vos.bootcamp.msbankaccounts.services.implementations;

import com.vos.bootcamp.msbankaccounts.models.BankAccount;
import com.vos.bootcamp.msbankaccounts.models.CustomerType;
import com.vos.bootcamp.msbankaccounts.repositories.IBankAccountRepository;
import com.vos.bootcamp.msbankaccounts.repositories.ICustomerRepository;
import com.vos.bootcamp.msbankaccounts.services.IBankAccountService;

import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class BankAccountServiceImpl implements IBankAccountService {

  private final IBankAccountRepository bankAccountRepository;
  private final ICustomerRepository customerRepository;

  public BankAccountServiceImpl(IBankAccountRepository bankAccountRepository,
                                ICustomerRepository customerRepository) {
    this.bankAccountRepository = bankAccountRepository;
    this.customerRepository = customerRepository;
  }

  @Override
  public Mono<Number> getCountBankAccounts(String numIdeDoc) {
    return Mono.from(bankAccountRepository.countAllByNumIdentityDocCustomer(numIdeDoc));
  }


  @Override
  public Mono<Number> getCountBankAccountsByType(String numIdeDoc, String type) {
    return Mono.from(bankAccountRepository.findByNumIdentityDocCustomer(numIdeDoc)
            .filter(bankAccount -> bankAccount.getBankAccountType().getName().equals(type))
            .count());
  }

  @Override
  public Mono<Boolean> validateRegisterCustomer(String numDoc, String type) {

    Mono<CustomerType> customerType = customerRepository.getCustomerType(numDoc);

    Mono<Boolean> existsCus = customerRepository.existsCustomer(numDoc);
    return existsCus.flatMap(resp -> {
      if (resp) {
        return customerType.flatMap(customerTypeRes -> {
          if (customerTypeRes.getName().equals("PERSONAL")) {
            log.info("Customer Type is Personal");
            if (type.equals("AHORRO") || type.equals("CUENTA CORRIENTE") || type.equals("CUENTA A PLAZO FIJO")) {
              return this.getCountBankAccountsByType(numDoc, type)
                      .flatMap(number -> {
                        if (number.intValue() < 2) {
                          log.info("La cuenta puede se puede crear!!");
                          return Mono.just(true);
                        }
                        log.error("cannot pass the maximum number of accounts per type");
                        return Mono.error(new Exception("Cannot pass the maximum number of accounts per type"));
                      });
            } else {
              log.error("Account type not supported");
              return Mono.error(new Exception("Account type not supported"));
            }
          } else if (customerTypeRes.getName().equals("EMPRESARIAL")) {
            log.info("Customer Type is Empresarial");
            if (type.equals("AHORRO") || type.equals("CUENTA A PLAZO FIJO")) {
              log.error("This customer cannot have these types of accounts");
              return Mono.error(new Exception("This customer cannot have these types of accounts"));
            } else if (type.equals("CUENTA CORRIENTE")) {
              return Mono.just(true);
            }
          }
          log.error("Customer Type not supported!!!");
          return Mono.error(new Exception("Customer Type not supported"));
        });
      } else {
        log.error("Customer not exists!!!");
        return Mono.error(new Exception("Customer not exist"));
      }
    });
  }


  @Override
  public Flux<BankAccount> findAll() {
    return bankAccountRepository.findAll();
  }

  @Override
  public Flux<BankAccount> findAllByNumIdeDoc(String numIdeDoc) {
    return bankAccountRepository.findByNumIdentityDocCustomer(numIdeDoc);
  }

  @Override
  public Mono<BankAccount> findByAccountNumber(String accountNumber) {
    return bankAccountRepository.findByAccountNumber(accountNumber);
  }

  @Override
  public Mono<BankAccount> findById(String id) {
    return bankAccountRepository.findById(id);
  }


  @Override
  public Mono<Boolean> existsByAccountNumber(String accountNumber) {
    return bankAccountRepository.existsByAccountNumber(accountNumber);
  }

  @Override
  public Mono<BankAccount> save(BankAccount bankAccount) {

    Mono<Boolean> validRes = this.validateRegisterCustomer(
            bankAccount.getNumIdentityDocCustomer(),
            bankAccount.getBankAccountType().getName());

    return validRes.flatMap(resValid -> {
      if (resValid) {
        bankAccount.setCreatedAt(new Date());
        return bankAccountRepository.save(bankAccount);
      } else {
        log.error("Ocurrio un problema, revisar las validaciones");
        return Mono.error(new Exception("Ocurrio un problema, revisar las validaciones"));
      }
    });
  }

  @Override
  public Mono<BankAccount> update(String id, BankAccount bankAccount) {
    return bankAccountRepository.findById(id)
            .flatMap(bankAccountDB -> {

              bankAccountDB.setUpdatedAt(new Date());

              if (bankAccount.getAmountAvailable() == null) {
                bankAccountDB.setAmountAvailable(bankAccountDB.getAmountAvailable());
              } else {
                bankAccountDB.setAmountAvailable(bankAccount.getAmountAvailable());
              }

              return bankAccountRepository.save(bankAccountDB);

            });
  }

  @Override
  public Mono<BankAccount> deleteById(String id) {
    return bankAccountRepository.findById(id)
            .flatMap(bankAccount -> bankAccountRepository.delete(bankAccount)
                    .then(Mono.just(bankAccount)));
  }

}
