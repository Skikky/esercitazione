package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prenotazione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dataOra;
    private int numeroPosti;

    @ManyToOne(optional = false)
    @JoinColumn(name = "utente_id")
    private Utente utente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ristorante_id")
    private Ristorante ristorante;
}
