package com.example.demo.services;

import com.example.demo.entities.Pietanza;
import com.example.demo.repositories.PietanzaRepository;
import com.example.demo.request.PietanzaRequest;
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

    public Pietanza create(PietanzaRequest pietanzaRequest) {
        Pietanza pietanza = Pietanza.builder()
                .nome(pietanzaRequest.getNome())
                .prezzo(pietanzaRequest.getPrezzo())
                .build();
        return pietanzaRepository.saveAndFlush(pietanza);
    }

    public Pietanza update(Long id, PietanzaRequest pietanzaRequest) {
        Pietanza pietanza = getPietanzaById(id);
        pietanza.setNome(pietanzaRequest.getNome());
        pietanza.setPrezzo(pietanzaRequest.getPrezzo());
        return pietanzaRepository.saveAndFlush(pietanza);
    }

    public void deletePietanza(Long id) {
        pietanzaRepository.deleteById(id);
    }
}
