package com.example.demo.response;


import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProprietarioResponse {
    private String nome;
    private String cognome;
    private String email;
    private String password;
    private List<Long> idRistoranti;
}
