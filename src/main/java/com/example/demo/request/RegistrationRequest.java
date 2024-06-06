package com.example.demo.request;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationRequest {
    private String nome;
    private String cognome;
    private String email;
    private String password;
    private Double saldo;
}
