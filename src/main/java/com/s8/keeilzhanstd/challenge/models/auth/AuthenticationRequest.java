package com.s8.keeilzhanstd.challenge.models.auth;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    @NotNull
    private String username;

    @NotNull
    private String password;
}
