package com.vos.bootcamp.msbankaccounts.controllers;

import com.vos.bootcamp.msbankaccounts.models.BankAccount;
import com.vos.bootcamp.msbankaccounts.services.IBankAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/bankAccounts")
@Api(value = "Bank Accounts Microservice")
public class BankAccountController {

  private final IBankAccountService service;

  public BankAccountController(IBankAccountService service) {
    this.service = service;
  }

  /* =====================================
    Function to List all Bank Accounts
  ===================================== */
  @GetMapping
  @ApiOperation(value = "List all BankAccounts", notes = "List all BankAccounts of Collections")
  public Flux<BankAccount> getBankAccounts() {
    return service.findAll();
  }

  /* ===============================================
       Function to obtain a bankAccount by id
  ============================================ */
  @GetMapping("/{id}")
  @ApiOperation(value = "Get a BankAccount", notes = "Get a BankAccount by id")
  public Mono<ResponseEntity<BankAccount>> getByIdBankAccount(@PathVariable String id) {
    return service.findById(id)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity
                    .notFound()
                    .build()
            );
  }

  /* ===============================================
     Function to obtain a bankAccount by accountNumber
  =================================================== */
  @GetMapping("/accountNumber/{accountNumber}")
  @ApiOperation(value = "Get a BankAccount", notes = "Get a BankAccount by accountNumber")
  public Mono<ResponseEntity<BankAccount>> getByAccountNumber(@PathVariable String accountNumber) {
    return service.findByAccountNumber(accountNumber)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity
                    .notFound()
                    .build()
            );

  }

  /* ===============================================
      Function to know if the Bank Account exists
   ================================================= */
  @GetMapping("/{accountNumber}/exist")
  @ApiOperation(value = "Bank Account exists", notes = "Validate if bank Account exists")
  public Mono<ResponseEntity<Boolean>> exitsBankAccount(@PathVariable String accountNumber) {
    return service.existsByAccountNumber(accountNumber)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity
                    .notFound()
                    .build()
            );
  }

  /* ===============================================
            Function to create a bankAccount
  =============================================== */
  @PostMapping
  @ApiOperation(value = "Create BankAccount", notes = "Create bankAccount, check the model please")
  public Mono<ResponseEntity<BankAccount>> createBankAccount(
          @Valid @RequestBody BankAccount bankAccount) {
    return service.save(bankAccount)
            .map(bankAccountTypeDB -> ResponseEntity
                    .created(URI.create("/api/bankAccountTypes/".concat(bankAccountTypeDB.getId())))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(bankAccountTypeDB)
            );
  }

  /* ===============================================
            Function to update a bankAccount by id
    =============================================== */
  @PutMapping("/{id}")
  @ApiOperation(value = "Update BankAccount", notes = "Update bankAccount by ID")
  public Mono<ResponseEntity<BankAccount>> updateBankAccount(@PathVariable String id,
                                                             @RequestBody BankAccount bankAccount) {
    return service.update(id, bankAccount)
            .map(bankAccountDB -> ResponseEntity
                    .created(URI.create("/api/bankAccountTypes/".concat(bankAccountDB.getId())))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(bankAccountDB))
            .defaultIfEmpty(ResponseEntity
                    .notFound()
                    .build()
            );
  }

  /* ===============================================
            Function to delete a bankAccount by id
  =============================================== */
  @DeleteMapping("/{id}")
  @ApiOperation(value = "Delete a BankAccount", notes = "Delete a bank account by ID")
  public Mono<ResponseEntity<Void>> deleteByIdBankAccount(@PathVariable String id) {
    return service.deleteById(id)
            .map(res -> ResponseEntity.ok().<Void>build())
            .defaultIfEmpty(ResponseEntity
                    .notFound()
                    .build()
            );

  }




}
