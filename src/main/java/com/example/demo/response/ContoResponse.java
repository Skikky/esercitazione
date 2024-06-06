package com.example.demo.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContoResponse {
    private Long id;
    private Long prenotazioneId;
    private Double totale;
    private LocalDateTime timestamp;
    private Boolean isPagato;
}
