package com.vos.bootcamp.msbankaccounts.repositories;

import com.vos.bootcamp.msbankaccounts.models.BankAccountType;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface IBankAccountTypeRepository extends ReactiveMongoRepository<BankAccountType, String> {
}
