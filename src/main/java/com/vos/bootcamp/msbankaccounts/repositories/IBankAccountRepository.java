package com.vos.bootcamp.msbankaccounts.repositories;

import com.vos.bootcamp.msbankaccounts.models.BankAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface IBankAccountRepository extends ReactiveMongoRepository<BankAccount, String> {
}
