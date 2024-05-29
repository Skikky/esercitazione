package com.example.demo.repositories;

import com.example.demo.entities.Comune;
import com.example.demo.entities.Ristorante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RistoranteRepository extends JpaRepository<Ristorante, Long> {
    @Query(value = "SELECT * FROM ristorante r WHERE r.comune_id = :comune_id", nativeQuery = true)
    List<Ristorante> findByComune(@Param("comune_id") Long comuneId);
}
