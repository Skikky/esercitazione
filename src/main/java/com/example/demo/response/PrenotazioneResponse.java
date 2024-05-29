package com.example.demo.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrenotazioneResponse {
    private Long idPrenotazione;
    private Long idUtente;
    private Long idRistorante;
    private LocalDateTime dataOra;
    private int numeroPosti;
}
