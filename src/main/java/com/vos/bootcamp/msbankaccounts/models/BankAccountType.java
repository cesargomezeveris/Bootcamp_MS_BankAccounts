package com.vos.bootcamp.msbankaccounts.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Document(collection = "ms_bankAccounts_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BankAccountType {

  @Id
  private String id;

  @NotBlank(message = "'Names' are required")
  private String name;

}




