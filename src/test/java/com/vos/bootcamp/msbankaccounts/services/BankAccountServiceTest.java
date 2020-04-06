package com.vos.bootcamp.msbankaccounts.services;

import com.vos.bootcamp.msbankaccounts.models.BankAccount;
import com.vos.bootcamp.msbankaccounts.models.BankAccountType;
import com.vos.bootcamp.msbankaccounts.models.Customer;
import com.vos.bootcamp.msbankaccounts.models.CustomerType;
import com.vos.bootcamp.msbankaccounts.repositories.IBankAccountRepository;
import com.vos.bootcamp.msbankaccounts.repositories.ICustomerRepository;
import com.vos.bootcamp.msbankaccounts.services.implementations.BankAccountServiceImpl;
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
public class BankAccountServiceTest {

  private final CustomerType customerType = CustomerType.builder().name("PERSONAL").build();
  private final CustomerType customerType2 = CustomerType.builder().name("CUSTOMER TYPE NOT SUPPORTED").build();

  private final Customer customer = Customer.builder().names("Vicse").surnames("Ore Soto").numIdentityDoc("75772936")
          .email("vicseore@gmail.com").phoneNumber("945026794").address("Calle 1 El Agustino").typeCustomer(customerType).build();
  private final Customer customer2 = Customer.builder().names("Cristian").surnames("Huaynates Soto").numIdentityDoc("34256278")
          .email("cheles@gmail.com").phoneNumber("990123568").address("Los frailes 401").typeCustomer(customerType2).build();

  private final BankAccountType bankAccountType1 = BankAccountType.builder().name("AHORRO").build();
  private final BankAccountType bankAccountType2 = BankAccountType.builder().name("CUENTA CORRIENTE").build();

  private final BankAccount bankAccount = BankAccount.builder().accountNumber("123-123-1223123")
          .numIdentityDocCustomer("75772936").bankAccountType(bankAccountType1).build();

  private final BankAccount bankAccount2 = BankAccount.builder().accountNumber("123-123-1223124")
          .numIdentityDocCustomer("75772936").bankAccountType(bankAccountType2).build();

  private final BankAccount bankAccount3 = BankAccount.builder().accountNumber("123-123-1223121")
          .numIdentityDocCustomer("75772936").bankAccountType(bankAccountType1).build();

  private final BankAccount bankAccount4 = BankAccount.builder().accountNumber("123-123-1223110")
          .numIdentityDocCustomer("34256278").bankAccountType(bankAccountType1).build();

  @Mock
  private IBankAccountRepository bankAccountRepository;

  @Mock
  private ICustomerRepository customerRepository;

  private IBankAccountService bankAccountService;

  @BeforeEach
  void SetUp(){
    bankAccountService = new BankAccountServiceImpl(bankAccountRepository, customerRepository) {
    };
  }

  @Test
  void getAll() {
    when(bankAccountRepository.findAll()).thenReturn(Flux.just(bankAccount, bankAccount2));

    Flux<BankAccount> actual = bankAccountService.findAll();

    assertResults(actual, bankAccount, bankAccount2);
  }

  @Test
  void getAllByNumDocCustomer() {
    when(bankAccountRepository.findByNumIdentityDocCustomer(customer.getNumIdentityDoc()))
            .thenReturn(Flux.just(bankAccount, bankAccount2));

    Flux<BankAccount> actual = bankAccountService.findAllByNumIdeDoc(customer.getNumIdentityDoc());

    assertResults(actual, bankAccount, bankAccount2);
  }

  @Test
  void validateIfBankAccount_When_Exist() {
    when(bankAccountRepository.existsByAccountNumber(bankAccount.getAccountNumber()))
            .thenReturn(Mono.just(true));

    Mono<Boolean> actual = bankAccountService.existsByAccountNumber(bankAccount.getAccountNumber());

    assertResults(actual, true);

  }

  @Test
  void validateIfBankAccount_When_NotExist() {
    when(bankAccountRepository.existsByAccountNumber(bankAccount.getAccountNumber()))
            .thenReturn(Mono.just(false));

    Mono<Boolean> actual = bankAccountService.existsByAccountNumber(bankAccount.getAccountNumber());

    assertResults(actual, false);

  }

  @Test
  void getCountBankAccountByNumDocCustomer() {
    when(bankAccountRepository.countAllByNumIdentityDocCustomer(bankAccount.getNumIdentityDocCustomer()))
            .thenReturn(Mono.just(2));

    Mono<Number> actual = bankAccountService.getCountBankAccounts(bankAccount.getNumIdentityDocCustomer());

    assertResults(actual, 2);

  }


  @Test
  void getAllByNumDocCustomer_WhenNumDocCustomer_NotExist() {
    when(bankAccountRepository.findByNumIdentityDocCustomer(customer.getNumIdentityDoc()))
            .thenReturn(Flux.empty());

    Flux<BankAccount> actual = bankAccountService.findAllByNumIdeDoc(customer.getNumIdentityDoc());

    assertResults(actual);
  }

  @Test
  void getById_whenIdExists_returnCorrectBankAccount() {
    when(bankAccountRepository.findById(bankAccount.getId())).thenReturn(Mono.just(bankAccount));

    Mono<BankAccount> actual = bankAccountService.findById(bankAccount.getId());

    assertResults(actual, bankAccount);
  }

  @Test
  void getById_whenIdNotExist_returnEmptyMono() {
    when(bankAccountRepository.findById(bankAccount.getId())).thenReturn(Mono.empty());

    Mono<BankAccount> actual = bankAccountService.findById(bankAccount.getId());

    assertResults(actual);
  }

  @Test
  void getByAccountNumber_whenAccountNumberExists_returnCorrectBankAccount() {
    when(bankAccountRepository.findByAccountNumber(bankAccount.getAccountNumber())).thenReturn(Mono.just(bankAccount));

    Mono<BankAccount> actual = bankAccountService.findByAccountNumber(bankAccount.getAccountNumber());

    assertResults(actual, bankAccount);
  }

  @Test
  void getById_whenAccountNotExist_returnEmptyMono() {
    when(bankAccountRepository.findByAccountNumber(bankAccount.getAccountNumber())).thenReturn(Mono.empty());

    Mono<BankAccount> actual = bankAccountService.findByAccountNumber(bankAccount.getAccountNumber());

    assertResults(actual);
  }

  @Test
  void createBankAccount() {

    when(customerRepository.existsCustomer(customer.getNumIdentityDoc()))
            .thenReturn(Mono.just(true));

    when(customerRepository.getCustomerType(customer.getNumIdentityDoc()))
            .thenReturn(Mono.just(customerType));

    when(bankAccountRepository.findByNumIdentityDocCustomer(customer.getNumIdentityDoc()))
            .thenReturn(Flux.just(bankAccount, bankAccount2));

    when(bankAccountRepository.save(bankAccount)).thenReturn(Mono.just(bankAccount));

    Mono<BankAccount> actual = bankAccountService.save(bankAccount);

    assertResults(actual, bankAccount);
  }

  @Test
  void createBankAccount_WhenCustomer_PassTheMaximum_NumberBankAccountPerType() {

    when(customerRepository.existsCustomer(customer.getNumIdentityDoc()))
            .thenReturn(Mono.just(true));

    when(customerRepository.getCustomerType(customer.getNumIdentityDoc()))
            .thenReturn(Mono.just(customerType));

    when(bankAccountRepository.findByNumIdentityDocCustomer(customer.getNumIdentityDoc()))
            .thenReturn(Flux.just(bankAccount, bankAccount2, bankAccount3));

    when(bankAccountRepository.save(bankAccount)).thenReturn(Mono.just(bankAccount));

    Mono<BankAccount> actual = bankAccountService.save(bankAccount);

    assertResults(actual, new Exception("Cannot pass the maximum number of accounts per type"));
  }

  @Test
  void createBankAccount_whenNotSupported_CustomerType() {

    when(customerRepository.existsCustomer(customer2.getNumIdentityDoc()))
            .thenReturn(Mono.just(true));

    when(customerRepository.getCustomerType(customer2.getNumIdentityDoc()))
            .thenReturn(Mono.just(customerType2));

    when(bankAccountRepository.findByNumIdentityDocCustomer(customer2.getNumIdentityDoc()))
            .thenReturn(Flux.empty());

    when(bankAccountRepository.save(bankAccount4)).thenReturn(Mono.just(bankAccount4));

    Mono<BankAccount> actual = bankAccountService.save(bankAccount4);

    assertResults(actual, new Exception("Customer Type not supported"));
  }


  @Test
  void update_whenIdExists_returnUpdatedBankAccount() {
    when(bankAccountRepository.findById(bankAccount.getId())).thenReturn(Mono.just(bankAccount));
    when(bankAccountRepository.save(bankAccount)).thenReturn(Mono.just(bankAccount));

    Mono<BankAccount> actual = bankAccountService.update(bankAccount.getId(), bankAccount);

    assertResults(actual, bankAccount);
  }

  @Test
  void update_whenIdNotExist_returnEmptyMono() {
    when(bankAccountRepository.findById(bankAccount.getId())).thenReturn(Mono.empty());

    Mono<BankAccount> actual = bankAccountService.update(bankAccount.getId(), bankAccount);

    assertResults(actual);
  }

  @Test
  void delete_whenBankAccountExists_performDeletion() {
    when(bankAccountRepository.findById(bankAccount.getId())).thenReturn(Mono.just(bankAccount));
    when(bankAccountRepository.delete(bankAccount)).thenReturn(Mono.empty());

    Mono<BankAccount> actual = bankAccountService.deleteById(bankAccount.getId());

    assertResults(actual, bankAccount);
  }

  @Test
  void delete_whenIdNotExist_returnEmptyMono() {
    when(bankAccountRepository.findById(bankAccount.getId())).thenReturn(Mono.empty());

    Mono<BankAccount> actual = bankAccountService.deleteById(bankAccount.getId());

    assertResults(actual);
  }




  /* ===============================================
           Validations of Customer
  ================================================== */
  @Test
  void validateRegisterCustomer_WhenCustomerExist() {
    when(customerRepository.existsCustomer(customer.getNumIdentityDoc()))
            .thenReturn(Mono.just(true));

    when(customerRepository.getCustomerType(customer.getNumIdentityDoc()))
            .thenReturn(Mono.just(customerType));

    when(bankAccountRepository.findByNumIdentityDocCustomer(customer.getNumIdentityDoc()))
            .thenReturn(Flux.just(bankAccount, bankAccount2));

    Mono<Boolean> actual = bankAccountService.validateRegisterCustomer(customer.getNumIdentityDoc(), bankAccountType1.getName());

    assertResults(actual, true);

  }

  @Test
  void validateRegisterCustomer_WhenCustomerNotExist() {
    when(customerRepository.existsCustomer(customer.getNumIdentityDoc()))
            .thenReturn(Mono.just(false));

    when(customerRepository.getCustomerType(customer.getNumIdentityDoc()))
            .thenReturn(Mono.just(customerType));

    when(bankAccountRepository.findByNumIdentityDocCustomer(customer.getNumIdentityDoc()))
            .thenReturn(Flux.just(bankAccount, bankAccount2));

    Mono<Boolean> actual = bankAccountService.validateRegisterCustomer(customer.getNumIdentityDoc(), bankAccountType1.getName());

    assertResults(actual, new Exception("Customer not exist"));

  }

  private void assertResults(Mono<Number> actual, int i) {
    StepVerifier
            .create(actual)
            .expectNext(i)
            .verifyComplete();
  }

  private void assertResults(Publisher<BankAccount> publisher, Exception exception) {
    StepVerifier
            .create(publisher)
            .expectErrorMessage(exception.getMessage())
            .verify();
  }


  private void assertResults(Publisher<BankAccount> publisher, BankAccount... expectedBankAccount) {
    StepVerifier
            .create(publisher)
            .expectNext(expectedBankAccount)
            .verifyComplete();
  }


  private void assertResults(Mono<Boolean> actual, Exception exception) {
    StepVerifier
            .create(actual)
            .expectErrorMessage(exception.getMessage())
            .verify();
  }

  private void assertResults(Mono<Boolean> actual, boolean b) {
    StepVerifier
            .create(actual)
            .expectNext(b)
            .verifyComplete();
  }



}
