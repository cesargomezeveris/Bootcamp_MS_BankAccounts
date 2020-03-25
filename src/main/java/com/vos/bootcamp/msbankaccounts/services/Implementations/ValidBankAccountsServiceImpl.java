package com.vos.bootcamp.msbankaccounts.services.Implementations;

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
    return WebClient.create()
            .get()
            .uri("http://localhost:8001/api/customers/" + id + "/exist")
            .retrieve()
            .bodyToMono(Boolean.class);

  }

  @Override
  public Mono<CustomerType> getCustomerType(String id) {
    return WebClient.create()
            .get()
            .uri("http://localhost:8001/api/customers/" + id + "/exist")
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
  public Mono<Boolean> validPersonalCustomer(String numDoc, String type) {

    Mono<CustomerType> customerType = this.getCustomerType(numDoc);

    return existsCustomer(numDoc).flatMap(resp -> {
      if (resp) {
        customerType.flatMap( customerTypeRes -> {
          if (customerTypeRes.getName().equals("Personal")) {
            if (type.equals("AHORRO") || type.equals("CUENTA CORRIENTE") || type.equals("CUENTA A PLAZO FIJO")) {
              this.getCountBankAccountsByType(numDoc, type)
                      .flatMap( number -> {
                        if (number.intValue() < 2 ) {
                          return Mono.just(true);
                        }
                        log.warn("cannot pass the maximum number of accounts per type");
                        return Mono.just(false);
                      });
            }
          } else if (customerTypeRes.getName().equals("Empresarial")) {
            if (type.equals("AHORRO") || type.equals("CUENTA A PLAZO FIJO") ) {
              log.warn("This customer cannot have these types of accounts");
              return Mono.just(false);
            }
            return Mono.just(true);
          }
          return Mono.just(false);
        });
      } else{
        log.warn("Customer not exists!!!");
        return Mono.just(false);
      }
      return Mono.just(false);
    });
  }


}
