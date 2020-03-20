package com.vos.bootcamp.msbankaccounts.controllers;

import com.vos.bootcamp.msbankaccounts.models.BankAccount;
import com.vos.bootcamp.msbankaccounts.services.IBankAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/bankAccounts")
@Api(value = "Bank Accounts Microservice")
public class BankAccountController {

  @Autowired
  private IBankAccountService service;

  /* =====================================
    Function to List all customers
  ===================================== */
  @GetMapping
  @ApiOperation(value = "List all BankAccounts", notes = "List all BankAccounts of Collections")
  public Mono<ResponseEntity<Flux<BankAccount>>> getCustomers() {
    return Mono.just(ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(service.findAll())
    );
  }




}
