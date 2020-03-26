package com.vos.bootcamp.msbankaccounts.services.implementations;

import com.vos.bootcamp.msbankaccounts.models.CustomerType;
import com.vos.bootcamp.msbankaccounts.repositories.IBankAccountRepository;
import com.vos.bootcamp.msbankaccounts.services.IValidBankAccountsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class ValidBankAccountsServiceImpl implements IValidBankAccountsService {

  @Autowired
  private IBankAccountRepository repository;

  @Override
  public Mono<Boolean> existsCustomer(String id) {
    return WebClient
            .create()
            .get()
            .uri("http://localhost:8001/api/customers/" + id + "/exist")
            .retrieve()
            .bodyToMono(Boolean.class);

  }

  @Override
  public Mono<CustomerType> getCustomerType(String id) {
    return WebClient
            .create()
            .get()
            .uri("http://localhost:8001/api/customers/" + id + "/customerType")
            .retrieve()
            .bodyToMono(CustomerType.class);
  }

  @Override
  public Mono<Number> getCountBankAccounts(String numIdeDoc) {
    return Mono.from(repository.findByNumIdentityDocCustomer(numIdeDoc).count());
  }


  @Override
  public Mono<Number> getCountBankAccountsByType(String numIdeDoc, String type) {
    return Mono.from(repository.findByNumIdentityDocCustomer(numIdeDoc)
            .filter(bankAccount -> bankAccount.getBankAccountType().getName().equals(type))
            .count());
  }

  @Override
  public Mono<Boolean> validateRegisterCustomer(String numDoc, String type) {

    Mono<CustomerType> customerType = this.getCustomerType(numDoc);

    Mono<Boolean> existsCus = this.existsCustomer(numDoc);
    return existsCus.flatMap(resp -> {
      if (resp) {
        return customerType.flatMap(customerTypeRes -> {
          if (customerTypeRes.getName().equals("Personal")) {
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
          } else if (customerTypeRes.getName().equals("Empresarial")) {
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


}
