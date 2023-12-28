package com.mock.interview.user.presentation.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    @Size(min = 3, max = 20)
    private String username;
    private String password;
}
