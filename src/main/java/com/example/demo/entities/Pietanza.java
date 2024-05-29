package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pietanza {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private Double prezzo;

    @ManyToMany(mappedBy = "menu")
    private Set<Ristorante> ristoranti = new HashSet<>();
}
