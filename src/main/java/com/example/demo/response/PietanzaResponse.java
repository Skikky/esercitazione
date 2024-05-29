package com.example.demo.response;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PietanzaResponse {
    private Long id;
    private String nome;
    private Double prezzo;
    private Set<Long> idRistoranti;
}
