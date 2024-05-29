package com.example.demo.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RistoranteRequest {
    private String nome;
    private String indirizzo;
    private Long idComune;
    private Long idProprietario;
    private Integer posti;
    private LocalTime apertura;
    private LocalTime chiusura;
    private Set<Long> idPietanze;
}
