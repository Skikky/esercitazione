package com.example.demo.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class KafkaContoDTO {
    private Long idConto;
    private Long idPrenotazione;
    private Long idUtente;
    private Long idRistorante;
    private Double totale;
    private Boolean isPagato;
    private LocalDateTime timestamp;
}
