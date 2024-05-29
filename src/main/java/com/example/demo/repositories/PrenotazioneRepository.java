package com.example.demo.repositories;

import com.example.demo.entities.Prenotazione;
import com.example.demo.entities.Ristorante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {
    List<Prenotazione> findByRistoranteAndDataOraBetween(Ristorante ristorante, LocalDateTime start, LocalDateTime end);
}
