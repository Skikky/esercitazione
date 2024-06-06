package com.example.demo.repositories;

import com.example.demo.entities.Conto;
import com.example.demo.entities.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContoRepository extends JpaRepository<Conto, Long> {
    Conto findByPrenotazione(Prenotazione prenotazione);
}
