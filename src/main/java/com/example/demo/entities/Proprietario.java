package com.example.demo.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Proprietario extends Utente {
    @OneToMany(mappedBy = "proprietario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ristorante> ristoranti;
}