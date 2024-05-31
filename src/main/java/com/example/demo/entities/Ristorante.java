package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ristorante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private String indirizzo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "comune_id", nullable = false)
    private Comune comune;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proprietario_id", nullable = false)
    private Proprietario proprietario;

    @Column(nullable = false)
    private int posti;
    @Column(nullable = false)
    private LocalTime apertura;
    @Column(nullable = false)
    private LocalTime chiusura;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "ristorante_pietanza",
            joinColumns = @JoinColumn(name = "ristorante_id"),
            inverseJoinColumns = @JoinColumn(name = "pietanza_id")
    )
    private Set<Pietanza> menu = new HashSet<>();
}
