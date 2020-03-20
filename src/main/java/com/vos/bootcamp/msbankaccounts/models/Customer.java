package com.vos.bootcamp.msbankaccounts.models;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Customer {

  @Id
  private String id;

  @NotBlank(message = "'Names' are required")
  private String names;

  @NotBlank(message = "'Surnames' are required")
  private String surnames;

  @NotBlank(message = "'Phone Number' is required")
  private String phoneNumber;

  @NotBlank(message = "'Address' is required")
  private String address;

  private CustomerType customerType;


}