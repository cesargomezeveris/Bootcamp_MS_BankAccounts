package com.vos.bootcamp.msbankaccounts.integration.controller;

import com.vos.bootcamp.msbankaccounts.commons.Constant;
import com.vos.bootcamp.msbankaccounts.models.BankAccountType;
import com.vos.bootcamp.msbankaccounts.repositories.IBankAccountTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class BankAccountTypeControllerIntegrationTest {

  @Autowired
  private ApplicationContext applicationContext;

  @Autowired
  private IBankAccountTypeRepository bankAccountTypeRepository;


  private final BankAccountType bankAccountType1 = BankAccountType.builder().name(Constant.CUENTA_AHORRO).build();
  private final BankAccountType bankAccountType2 = BankAccountType.builder().name(Constant.CUENTA_CORRIENTE).build();
  private final BankAccountType bankAccountType3 = BankAccountType.builder().name(Constant.CUENTA_A_PLAZO_FIJO).build();
  private final BankAccountType bankAccountType4 = BankAccountType.builder().name(Constant.CUENTA_AHORRO_PERSONAL_VIP).build();
  private final BankAccountType bankAccountType5 = BankAccountType.builder().name(Constant.CUENTA_CORRIENTE_PERSONAL_VIP).build();
  private final BankAccountType bankAccountType6 = BankAccountType.builder().name(Constant.CUENTA_PLAZO_FIJO_VIP).build();
  private final BankAccountType bankAccountType7 = BankAccountType.builder().name(Constant.CUENTA_EMPRESARIAL_PYME).build();
  private final BankAccountType bankAccountType8 = BankAccountType.builder().name(Constant.CUENTA_EMPRESARIAL_CORPORATIVO).build();

  private WebTestClient client;

  private List<BankAccountType> expectedBankAccountTypes;

  @BeforeEach
  void setUp() {
    client = WebTestClient
            .bindToApplicationContext(applicationContext)
            .configureClient()
            .baseUrl("/api/bankAccountTypes")
            .build();

    Flux<BankAccountType> initData = bankAccountTypeRepository.deleteAll()
            .thenMany(Flux.just(
                    bankAccountType1, bankAccountType2, bankAccountType3, bankAccountType4,
                    bankAccountType5, bankAccountType6, bankAccountType7, bankAccountType8)
                    .flatMap(bankAccountTypeRepository::save))
            .thenMany(bankAccountTypeRepository.findAll());

    expectedBankAccountTypes = initData.collectList().block();
  }

  @Test
  void getAllBankAccountTypes() {
    client.get()
            .uri("/")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBodyList(BankAccountType.class)
            .isEqualTo(expectedBankAccountTypes)
    ;
  }

  @Test
  void getBankAccountTypeById_whenBankAccountTypeExists_returnCorrectBankAccountType() {
    BankAccountType expectedBankAccountType = expectedBankAccountTypes.get(0);

    client.get()
            .uri("/{id}", expectedBankAccountType.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(BankAccountType.class)
            .isEqualTo(expectedBankAccountType)
    ;
  }

  @Test
  void getBankAccountTypeById_whenBankAccountTypeNotExist_returnNotFound() {
    String id = "NOT_EXIST_ID";

    client.get()
            .uri("/{id}", id)
            .exchange()
            .expectStatus()
            .isNotFound()
    ;
  }

  @Test
  void addBankAccountType() {
    BankAccountType expectedBankAccountType = expectedBankAccountTypes.get(0);

    client.post()
            .uri("/")
            .body(Mono.just(expectedBankAccountType), BankAccountType.class)
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(BankAccountType.class)
            .isEqualTo(expectedBankAccountType)
    ;
  }

  @Test
  void addBankAccountType_whenBankAccountTypeIsInvalid_returnBadRequest() {
    BankAccountType bankAccountType = BankAccountType.builder().name("").build();

    client.post()
            .uri("/")
            .body(Mono.just(bankAccountType), BankAccountType.class)
            .exchange()
            .expectStatus()
            .isBadRequest()
    ;
  }

  @Test
  void updateBankAccountType_whenBankAccountTypeExists_performUpdate() {
    BankAccountType expectedBankAccountType = expectedBankAccountTypes.get(0);

    client.put()
            .uri("/{id}", expectedBankAccountType.getId())
            .body(Mono.just(expectedBankAccountType), BankAccountType.class)
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(BankAccountType.class)
            .isEqualTo(expectedBankAccountType)
    ;
  }

  @Test
  void updateBankAccountType_whenBankAccountTypeNotExist_returnNotFound() {
    String id = "NOT_EXIST_ID";
    BankAccountType expectedBankAccountType = expectedBankAccountTypes.get(0);

    client.put().uri("/{id}", id)
            .body(Mono.just(expectedBankAccountType), BankAccountType.class)
            .exchange()
            .expectStatus()
            .isNotFound()
    ;
  }

  @Test
  void deleteBankAccountType_whenProductExists_performDeletion() {
    BankAccountType bankAccountTypeToDelete = expectedBankAccountTypes.get(0);

    client.delete()
            .uri("/{id}", bankAccountTypeToDelete.getId())
            .exchange()
            .expectStatus()
            .isOk()
    ;
  }

  @Test
  void deleteBankAccountType_whenIdNotExist_returnNotFound() {
    String id = "NOT_EXIST_ID";

    client.delete()
            .uri("/{id}", id)
            .exchange()
            .expectStatus()
            .isNotFound();
  }



}
