package com.vos.bootcamp.msbankaccounts.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "ms_bankAccounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BankAccount {

  @Id
  private String id;

  @Indexed(unique = true)
  @NotBlank(message = "'accountNumber' is required")
  private String accountNumber;

  @NotBlank(message = "'numIdentityDoc' is required")
  private String numIdentityDocCustomer;

  private Double amountAvailable;

  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="America/Lima")
  private Date createdAt;

  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="America/Lima")
  private Date updatedAt;

  @Valid
  @DBRef
  private BankAccountType bankAccountType;

}
