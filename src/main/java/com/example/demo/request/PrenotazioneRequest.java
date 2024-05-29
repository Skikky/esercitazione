package com.example.demo.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrenotazioneRequest {
    private Long idUtente;
    private Long idRistorante;
    private LocalDateTime dataPrenotazione;
    private int numeroPosti;
}
