package com.example.demo.repositories;

import com.example.demo.entities.Pietanza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PietanzaRepository extends JpaRepository<Pietanza, Long> {
}
