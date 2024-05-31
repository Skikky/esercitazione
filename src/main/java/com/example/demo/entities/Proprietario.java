package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Proprietario extends Utente {
    @JsonIgnore
    @OneToMany(mappedBy = "proprietario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ristorante> ristoranti;

    @PreRemove
    private void preRemove() {
        for (Ristorante ristorante : ristoranti) {
            ristorante.setProprietario(null);
        }
    }
}