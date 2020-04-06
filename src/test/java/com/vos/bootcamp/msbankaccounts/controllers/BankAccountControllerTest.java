package com.vos.bootcamp.msbankaccounts.controllers;

import com.vos.bootcamp.msbankaccounts.models.BankAccount;
import com.vos.bootcamp.msbankaccounts.models.BankAccount;
import com.vos.bootcamp.msbankaccounts.models.BankAccountType;
import com.vos.bootcamp.msbankaccounts.services.IBankAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class BankAccountControllerTest {

  @Mock
  private IBankAccountService bankAccountService;
  private WebTestClient client;
  private List<BankAccount> expectedBankAccounts;

  private final BankAccountType bankAccountType1 = BankAccountType.builder().id("1").name("AHORRO").build();
  private final BankAccountType bankAccountType2 = BankAccountType.builder().id("2").name("CUENTA CORRIENTE").build();


  @BeforeEach
  void setUp() {
    client = WebTestClient
            .bindToController(new BankAccountController(bankAccountService))
            .configureClient()
            .baseUrl("/api/bankAccounts")
            .build();

    expectedBankAccounts = Arrays.asList(
            BankAccount.builder().id("1").accountNumber("123-123-1223123")
                    .numIdentityDocCustomer("75772936").amountAvailable(500.0).bankAccountType(bankAccountType1).build(),
            BankAccount.builder().id("2").accountNumber("123-123-1223124")
                    .numIdentityDocCustomer("75772936").amountAvailable(70.0).bankAccountType(bankAccountType2).build(),
            BankAccount.builder().id("3").accountNumber("123-123-1223125")
                    .numIdentityDocCustomer("75772936").amountAvailable(1200.0).bankAccountType(bankAccountType1).build());

  }

  @Test
  void getAll() {
    when(bankAccountService.findAll()).thenReturn(Flux.fromIterable(expectedBankAccounts));

    client.get()
            .uri("/")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBodyList(BankAccount.class)
            .isEqualTo(expectedBankAccounts);
  }

  @Test
  void getBankAccount_WhenBankAccountExist_returnTrue() {
    BankAccount expectedBankAccount = expectedBankAccounts.get(0);
    when(bankAccountService.existsByAccountNumber(expectedBankAccount.getAccountNumber()))
            .thenReturn(Mono.just(true));

    client.get()
            .uri("/{accountNumber}/exist", expectedBankAccount.getAccountNumber())
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(Boolean.class)
            .isEqualTo(true);
  }

  @Test
  void getBankAccount_WhenBankAccountNotExist_returnFalse() {
    String accountNumber = "NOT_EXIST_ID";
    when(bankAccountService.existsByAccountNumber(accountNumber))
            .thenReturn(Mono.just(false));

    client.get()
            .uri("/{accountNumber}/exist", accountNumber)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(Boolean.class)
            .isEqualTo(false);
  }

  @Test
  void getBankAccountById_whenBankAccountExists_returnCorrectBankAccount() {
    BankAccount expectedBankAccount = expectedBankAccounts.get(0);
    when(bankAccountService.findById(expectedBankAccount.getId()))
            .thenReturn(Mono.just(expectedBankAccount));

    client.get()
            .uri("/{id}", expectedBankAccount.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(BankAccount.class)
            .isEqualTo(expectedBankAccount);
  }

  @Test
  void getBankAccountById_whenBankAccountNotExist_returnNotFound() {
    String id = "NOT_EXIST_ID";
    when(bankAccountService.findById(id)).thenReturn(Mono.empty());

    client.get()
            .uri("/{id}", id)
            .exchange()
            .expectStatus()
            .isNotFound();
  }

  @Test
  void getBankAccountByAccountNumber_whenBankAccountExists_returnCorrectBankAccount() {
    BankAccount expectedBankAccount = expectedBankAccounts.get(0);
    when(bankAccountService.findByAccountNumber(expectedBankAccount.getAccountNumber()))
            .thenReturn(Mono.just(expectedBankAccount));

    client.get()
            .uri("/accountNumber/{accountNumber}", expectedBankAccount.getAccountNumber())
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(BankAccount.class)
            .isEqualTo(expectedBankAccount);
  }

  @Test
  void getBankAccountByAccountNumber_whenBankAccountNotExist_returnNotFound() {
    String accountNumber = "NOT_EXIST_ID";
    when(bankAccountService.findByAccountNumber(accountNumber)).thenReturn(Mono.empty());

    client.get()
            .uri("/accountNumber/{accountNumber}", accountNumber)
            .exchange()
            .expectStatus()
            .isNotFound();
  }

  @Test
  void addBankAccount() {
    BankAccount expectedBankAccount = expectedBankAccounts.get(0);
    when(bankAccountService.save(expectedBankAccount))
            .thenReturn(Mono.just(expectedBankAccount));

    client.post()
            .uri("/")
            .body(Mono.just(expectedBankAccount), BankAccount.class)
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(BankAccount.class)
            .isEqualTo(expectedBankAccount);
  }


  @Test
  void updateBankAccount_whenBankAccountExists_performUpdate() {
    BankAccount expectedBankAccount = expectedBankAccounts.get(0);
    when(bankAccountService.update(expectedBankAccount.getId(), expectedBankAccount))
            .thenReturn(Mono.just(expectedBankAccount));

    client.put()
            .uri("/{id}", expectedBankAccount.getId())
            .body(Mono.just(expectedBankAccount), BankAccount.class)
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(BankAccount.class)
            .isEqualTo(expectedBankAccount);
  }

  @Test
  void updateBankAccount_whenBankAccountNotExist_returnNotFound() {
    String id = "NOT_EXIST_ID";
    BankAccount expectedBankAccount = expectedBankAccounts.get(0);
    when(bankAccountService.update(id, expectedBankAccount)).thenReturn(Mono.empty());

    client.put()
            .uri("/{id}", id)
            .body(Mono.just(expectedBankAccount), BankAccount.class)
            .exchange()
            .expectStatus()
            .isNotFound();
  }

  @Test
  void deleteBankAccount_whenBankAccountExists_performDeletion() {
    BankAccount bankAccountToDelete = expectedBankAccounts.get(0);
    when(bankAccountService.deleteById(bankAccountToDelete.getId()))
            .thenReturn(Mono.just(bankAccountToDelete));

    client.delete()
            .uri("/{id}", bankAccountToDelete.getId())
            .exchange()
            .expectStatus()
            .isOk();
  }

  @Test
  void deleteBankAccount_whenIdNotExist_returnNotFound() {
    BankAccount bankAccountToDelete = expectedBankAccounts.get(0);
    when(bankAccountService.deleteById(bankAccountToDelete.getId()))
            .thenReturn(Mono.empty());

    client.delete()
            .uri("/{id}", bankAccountToDelete.getId())
            .exchange()
            .expectStatus()
            .isNotFound();
  }
  


}
