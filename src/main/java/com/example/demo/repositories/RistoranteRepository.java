package com.example.demo.repositories;

import com.example.demo.entities.Ristorante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RistoranteRepository extends JpaRepository<Ristorante, Long> {
    @Query("SELECT r FROM Ristorante r WHERE r.comune.id = :comuneId")
    List<Ristorante> findByComuneId(@Param("comuneId") Long comuneId);
}
