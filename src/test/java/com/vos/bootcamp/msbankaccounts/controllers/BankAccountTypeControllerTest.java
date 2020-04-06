package com.vos.bootcamp.msbankaccounts.controllers;

import com.vos.bootcamp.msbankaccounts.models.BankAccountType;
import com.vos.bootcamp.msbankaccounts.services.IBankAccountTypeService;
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
public class BankAccountTypeControllerTest {

  @Mock
  private IBankAccountTypeService bankAccountTypeService;
  private WebTestClient client;
  private List<BankAccountType> expectedBankAccountsTypes;

  @BeforeEach
  void setUp() {
    client = WebTestClient
            .bindToController(new BankAccountTypeController(bankAccountTypeService))
            .configureClient()
            .baseUrl("/api/bankAccountTypes")
            .build();

    expectedBankAccountsTypes = Arrays.asList(
            BankAccountType.builder().id("1").name("AHORRO").build(),
            BankAccountType.builder().id("2").name("CUENTA A PLAZO FIJO").build(),
            BankAccountType.builder().id("3").name("CUENTA CORRIENTE").build());

  }

  @Test
  void getAll() {
    when(bankAccountTypeService.findAll()).thenReturn(Flux.fromIterable(expectedBankAccountsTypes));

    client.get()
            .uri("/")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBodyList(BankAccountType.class)
            .isEqualTo(expectedBankAccountsTypes);
  }

  @Test
  void getBankAccountTypeById_whenBankAccountTypeExists_returnCorrectBankAccountType() {
    BankAccountType expectedBankAccountType = expectedBankAccountsTypes.get(0);
    when(bankAccountTypeService.findById(expectedBankAccountType.getId()))
            .thenReturn(Mono.just(expectedBankAccountType));

    client.get()
            .uri("/{id}", expectedBankAccountType.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(BankAccountType.class)
            .isEqualTo(expectedBankAccountType);
  }

  @Test
  void getBankAccountTypeById_whenBankAccountTypeNotExist_returnNotFound() {
    String id = "NOT_EXIST_ID";
    when(bankAccountTypeService.findById(id)).thenReturn(Mono.empty());

    client.get()
            .uri("/{id}", id)
            .exchange()
            .expectStatus()
            .isNotFound();
  }

  @Test
  void addBankAccountType() {
    BankAccountType expectedBankAccountType = expectedBankAccountsTypes.get(0);
    when(bankAccountTypeService.save(expectedBankAccountType))
            .thenReturn(Mono.just(expectedBankAccountType));

    client.post()
            .uri("/")
            .body(Mono.just(expectedBankAccountType), BankAccountType.class)
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(BankAccountType.class)
            .isEqualTo(expectedBankAccountType);
  }

  @Test
  void updateBankAccountType_whenBankAccountTypeExists_performUpdate() {
    BankAccountType expectedBankAccountType = expectedBankAccountsTypes.get(0);
    when(bankAccountTypeService.update(expectedBankAccountType.getId(), expectedBankAccountType))
            .thenReturn(Mono.just(expectedBankAccountType));

    client.put()
            .uri("/{id}", expectedBankAccountType.getId())
            .body(Mono.just(expectedBankAccountType), BankAccountType.class)
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(BankAccountType.class)
            .isEqualTo(expectedBankAccountType);
  }

  @Test
  void updateBankAccountType_whenBankAccountTypeNotExist_returnNotFound() {
    String id = "NOT_EXIST_ID";
    BankAccountType expectedBankAccountType = expectedBankAccountsTypes.get(0);
    when(bankAccountTypeService.update(id, expectedBankAccountType)).thenReturn(Mono.empty());

    client.put()
            .uri("/{id}", id)
            .body(Mono.just(expectedBankAccountType), BankAccountType.class)
            .exchange()
            .expectStatus()
            .isNotFound();
  }

  @Test
  void deleteBankAccountType_whenBankAccountTypeExists_performDeletion() {
    BankAccountType bankAccountTypeToDelete = expectedBankAccountsTypes.get(0);
    when(bankAccountTypeService.deleteById(bankAccountTypeToDelete.getId()))
            .thenReturn(Mono.just(bankAccountTypeToDelete));

    client.delete()
            .uri("/{id}", bankAccountTypeToDelete.getId())
            .exchange()
            .expectStatus()
            .isOk();
  }

  @Test
  void deleteBankAccountType_whenIdNotExist_returnNotFound() {
    BankAccountType bankAccountTypeToDelete = expectedBankAccountsTypes.get(0);
    when(bankAccountTypeService.deleteById(bankAccountTypeToDelete.getId()))
            .thenReturn(Mono.empty());

    client.delete()
            .uri("/{id}", bankAccountTypeToDelete.getId())
            .exchange()
            .expectStatus()
            .isNotFound();
  }



}
