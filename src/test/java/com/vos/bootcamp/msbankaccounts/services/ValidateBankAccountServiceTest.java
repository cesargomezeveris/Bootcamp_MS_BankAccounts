package com.vos.bootcamp.msbankaccounts.services;

import com.vos.bootcamp.msbankaccounts.models.BankAccount;
import com.vos.bootcamp.msbankaccounts.models.BankAccountType;
import com.vos.bootcamp.msbankaccounts.repositories.IBankAccountRepository;
import com.vos.bootcamp.msbankaccounts.services.implementations.ValidateBankAccountsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.reactivestreams.Publisher;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ValidateBankAccountServiceTest {

  private final BankAccountType bankAccountType1 = BankAccountType.builder().name("AHORRO").build();
  private final BankAccountType bankAccountType2 = BankAccountType.builder().name("CUENTA CORRIENTE").build();

  private final BankAccount bankAccount = BankAccount.builder().accountNumber("123-123-1223123")
          .numIdentityDocCustomer("75772936").bankAccountType(bankAccountType1).build();
  private final BankAccount bankAccount2 = BankAccount.builder().accountNumber("123-123-1223124")
          .numIdentityDocCustomer("75772936").bankAccountType(bankAccountType2).build();


  @Mock
  private IBankAccountRepository bankAccountRepository;

  private IValidateBankAccountsService validateBankAccountsService;

  @BeforeEach
  void SetUp(){
    validateBankAccountsService = new ValidateBankAccountsServiceImpl(bankAccountRepository) {
    };
  }


  @Test
  void GetCountBankAccountsIfCustomerExist() {
    final Number countBankAccounts = 3;
    when(bankAccountRepository.countAllByNumIdentityDocCustomer(bankAccount.getNumIdentityDocCustomer()))
                          .thenReturn(Mono.from(Flux.just(bankAccount, bankAccount2, bankAccount).count()));

    Mono<Number> actual = validateBankAccountsService.getCountBankAccounts(bankAccount.getNumIdentityDocCustomer());

    assertResults(actual, countBankAccounts);
  }

  private void assertResults(Mono<Number> actual,Number expectNumber) {
    StepVerifier
            .create(actual)
            .expectNext(expectNumber)
            .verifyComplete();
  }

  private void assertResults(Publisher<BankAccount> publisher, BankAccount... expectedBankAccount) {
    StepVerifier
            .create(publisher)
            .expectNext(expectedBankAccount)
            .verifyComplete();
  }


}
