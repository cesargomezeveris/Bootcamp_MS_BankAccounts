package com.vos.bootcamp.msbankaccounts.services;

import com.vos.bootcamp.msbankaccounts.models.BankAccountType;
import com.vos.bootcamp.msbankaccounts.util.ICrud;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IBankAccountTypeService extends ICrud<BankAccountType> {

}
