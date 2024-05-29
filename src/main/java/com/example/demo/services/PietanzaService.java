package com.example.demo.services;

import com.example.demo.entities.Pietanza;
import com.example.demo.repositories.PietanzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PietanzaService {
    @Autowired
    private PietanzaRepository pietanzaRepository;

    public Pietanza getPietanzaById(Long id) {
        Optional<Pietanza> pietanza = pietanzaRepository.findById(id);
        return pietanza.orElseThrow(() -> new IllegalArgumentException("pietanza non trovata con id: "+id));
    }

    public List<Pietanza> getAllPietanze() {
        return pietanzaRepository.findAll();
    }

    public Pietanza create(Pietanza pietanza) {
        return pietanzaRepository.saveAndFlush(pietanza);
    }

    public Pietanza update(Long id, Pietanza newPietanza) {
        Pietanza pietanza = getPietanzaById(id);
        pietanza = Pietanza.builder()
                .nome(newPietanza.getNome())
                .prezzo(newPietanza.getPrezzo())
                .build();
        return pietanzaRepository.saveAndFlush(pietanza);
    }

    public void deletePietanza(Long id) {
        pietanzaRepository.deleteById(id);
    }
}
