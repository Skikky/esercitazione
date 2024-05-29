package com.example.demo.response;

import lombok.*;

import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RistoranteResponse {
    private Long idRistorante;
    private String nome;
    private String indirizzo;
    private Long idComune;
    private Long idProprietario;
    private Integer posti;
    private LocalTime apertura;
    private LocalTime chiusura;
    private Set<Long> idPietanze;
}
