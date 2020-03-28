package com.vos.bootcamp.msbankaccounts.services;

import com.vos.bootcamp.msbankaccounts.models.BankAccountType;
import com.vos.bootcamp.msbankaccounts.repositories.IBankAccountTypeRepository;
import com.vos.bootcamp.msbankaccounts.services.implementations.BankAccountTypeServiceImpl;
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
public class BankAccountTypeServiceTest {

  private final BankAccountType bankAccountType1 = BankAccountType.builder().name("AHORRO").build();
  private final BankAccountType bankAccountType2 = BankAccountType.builder().name("CUENTA A PLAZO FIJO").build();
  private final BankAccountType bankAccountType3 = BankAccountType.builder().name("CUENTA CORRIENTE").build();

  @Mock
  private IBankAccountTypeRepository bankAccountTypeRepository;

  private IBankAccountTypeService bankAccountTypeService;

  @BeforeEach
  void SetUp(){
    bankAccountTypeService = new BankAccountTypeServiceImpl(bankAccountTypeRepository) {
    };
  }

  @Test
  void getAll() {
    when(bankAccountTypeRepository.findAll()).thenReturn(Flux.just(bankAccountType1, bankAccountType2, bankAccountType3));

    Flux<BankAccountType> actual = bankAccountTypeService.findAll();

    assertResults(actual, bankAccountType1, bankAccountType2, bankAccountType3);
  }

  @Test
  void getById_whenIdExists_returnCorrectBankAccountType() {
    when(bankAccountTypeRepository.findById(bankAccountType1.getId())).thenReturn(Mono.just(bankAccountType1));

    Mono<BankAccountType> actual = bankAccountTypeService.findById(bankAccountType1.getId());

    assertResults(actual, bankAccountType1);
  }

  @Test
  void getById_whenIdNotExist_returnEmptyMono() {
    when(bankAccountTypeRepository.findById(bankAccountType1.getId())).thenReturn(Mono.empty());

    Mono<BankAccountType> actual = bankAccountTypeService.findById(bankAccountType1.getId());

    assertResults(actual);
  }

  @Test
  void create() {
    when(bankAccountTypeRepository.save(bankAccountType1)).thenReturn(Mono.just(bankAccountType1));

    Mono<BankAccountType> actual = bankAccountTypeService.save(bankAccountType1);

    assertResults(actual, bankAccountType1);
  }

  @Test
  void update_whenIdExists_returnUpdatedBankAccountType() {
    when(bankAccountTypeRepository.findById(bankAccountType1.getId())).thenReturn(Mono.just(bankAccountType1));
    when(bankAccountTypeRepository.save(bankAccountType1)).thenReturn(Mono.just(bankAccountType1));

    Mono<BankAccountType> actual = bankAccountTypeService.update(bankAccountType1.getId(), bankAccountType1);

    assertResults(actual, bankAccountType1);
  }

  @Test
  void update_whenIdNotExist_returnEmptyMono() {
    when(bankAccountTypeRepository.findById(bankAccountType1.getId())).thenReturn(Mono.empty());

    Mono<BankAccountType> actual = bankAccountTypeService.update(bankAccountType1.getId(), bankAccountType1);

    assertResults(actual);
  }

  @Test
  void delete_whenBankAccountTypeExists_performDeletion() {
    when(bankAccountTypeRepository.findById(bankAccountType1.getId())).thenReturn(Mono.just(bankAccountType1));
    when(bankAccountTypeRepository.delete(bankAccountType1)).thenReturn(Mono.empty());

    Mono<BankAccountType> actual = bankAccountTypeService.deleteById(bankAccountType1.getId());

    assertResults(actual, bankAccountType1);
  }

  @Test
  void delete_whenIdNotExist_returnEmptyMono() {
    when(bankAccountTypeRepository.findById(bankAccountType1.getId())).thenReturn(Mono.empty());

    Mono<BankAccountType> actual = bankAccountTypeService.deleteById(bankAccountType1.getId());

    assertResults(actual);
  }


  private void assertResults(Publisher<BankAccountType> publisher, BankAccountType... expectedBankAccountTypes) {
    StepVerifier
            .create(publisher)
            .expectNext(expectedBankAccountTypes)
            .verifyComplete();
  }




}
