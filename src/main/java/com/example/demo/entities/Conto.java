package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Conto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    private Prenotazione prenotazione;
    @Column(nullable = false)
    @Check(constraints = "totale > 0")
    private Double totale;
    @Column(nullable = false)
    private LocalDateTime timestamp;
    @Column (nullable = false)
    private Boolean isPagato;

}
