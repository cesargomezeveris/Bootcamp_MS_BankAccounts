package com.vos.bootcamp.msbankaccounts.models;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "ms_bankAccounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BankAccount {

  @Id
  private String id;

  @NotBlank(message = "'accountNumber' is required")
  private String accountNumber;

  @NotBlank(message = "'numIdentityDoc' is required")
  private String numIdentityDocCustomer;

}
