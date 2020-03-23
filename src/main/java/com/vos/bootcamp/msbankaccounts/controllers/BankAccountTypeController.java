package com.vos.bootcamp.msbankaccounts.controllers;

import com.vos.bootcamp.msbankaccounts.models.BankAccountType;
import com.vos.bootcamp.msbankaccounts.services.IBankAccountTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/bankAccountTypes")
@Api(value = "Bank Accounts Microservice")
public class BankAccountTypeController {

  @Autowired
  private IBankAccountTypeService service;

  /* =====================================
    Function to List all bankAccounts Types
  ===================================== */
  @GetMapping
  @ApiOperation(value = "List all bankAccountTypes", notes = "List all BankAccountsTypes of Collections")
  public Flux<BankAccountType> getBankAccountsTypes() {
    return service.findAll();
  }

  /* ===============================================
       Function to obtain a bankAccountType by id
  ============================================ */
  @GetMapping("/{id}")
  @ApiOperation(value = "Get a bankAccountTypes", notes = "Get a bankAccountTypes by id")
  public Mono<ResponseEntity<BankAccountType>> getByIdBankAccountTypes(@PathVariable String id) {
    return service.findById(id)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity
                    .notFound()
                    .build()
            );
  }

  /* ===============================================
            Function to create a bankAccountType
    =============================================== */

  @PostMapping
  @ApiOperation(value = "Create BankAccountTypes", notes="Create bankAccountTypes, check the model please")
  public Mono<ResponseEntity<BankAccountType>> createBankAccountTypes(@Valid @RequestBody BankAccountType bankAccountType){
    return service.save(bankAccountType)
            .map(bankAccountTypeDB -> ResponseEntity
                    .created(URI.create("/api/bankAccountTypes/".concat(bankAccountTypeDB.getId())))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(bankAccountTypeDB)
            );
  }

  /* ===============================================
            Function to update a bankAccountType by id
    =============================================== */

  @PutMapping("/{id}")
  @ApiOperation(value = "Update BankAccountTypes", notes="Update bankAccountTypes by ID")
  public Mono<ResponseEntity<BankAccountType>> updateBankAccountTypes(@PathVariable String id, @RequestBody BankAccountType bankAccountType) {
    return service.update(id, bankAccountType)
            .map(bankAccountTypeDB -> ResponseEntity
                    .created(URI.create("/api/bankAccountTypes/".concat(bankAccountTypeDB.getId())))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(bankAccountTypeDB))
            .defaultIfEmpty(ResponseEntity
                    .notFound()
                    .build()
            );
  }

  /* ===============================================
            Function to delete a bankAccountType by id
    =============================================== */

  @DeleteMapping("/{id}")
  @ApiOperation(value = "Delete a BankAccountType", notes="Delete a bank account Type by ID")
  public Mono<ResponseEntity<Void>> deleteByIdBankAccountTypes(@PathVariable String id) {
    return service.deleteById(id)
            .map(res -> ResponseEntity.ok().<Void>build())
            .defaultIfEmpty(ResponseEntity
                    .notFound()
                    .build()
            );

  }


}
