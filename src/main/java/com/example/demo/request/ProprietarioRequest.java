package com.example.demo.request;


import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProprietarioRequest {
    private String nome;
    private String cognome;
    private String email;
    private String password;
    private List<Long> idRistoranti;
}
