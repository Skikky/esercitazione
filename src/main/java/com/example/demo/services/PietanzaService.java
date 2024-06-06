package com.example.demo.services;

import com.example.demo.entities.Pietanza;
import com.example.demo.entities.Ristorante;
import com.example.demo.repositories.PietanzaRepository;
import com.example.demo.repositories.RistoranteRepository;
import com.example.demo.request.PietanzaRequest;
import com.example.demo.response.PietanzaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PietanzaService {
    @Autowired
    private PietanzaRepository pietanzaRepository;
    @Autowired
    private RistoranteRepository ristoranteRepository;

    public Pietanza getPietanzaById(Long id) {
        Optional<Pietanza> pietanza = pietanzaRepository.findById(id);
        return pietanza.orElseThrow(() -> new IllegalArgumentException("pietanza non trovata con id: "+id));
    }

    private PietanzaResponse mapToPietanzaResponse(Pietanza pietanza) {
        Set<Long> idRistoranti = Collections.emptySet();
        if (pietanza.getRistoranti() != null && !pietanza.getRistoranti().isEmpty()) {
            idRistoranti = pietanza.getRistoranti().stream()
                    .map(Ristorante::getId)
                    .collect(Collectors.toSet());
        }

        return PietanzaResponse.builder()
                .id(pietanza.getId())
                .nome(pietanza.getNome())
                .prezzo(pietanza.getPrezzo())
                .idRistoranti(idRistoranti)
                .build();
    }

    public PietanzaResponse getPietanzaResponseById(Long id) {
        Optional<Pietanza> pietanza = pietanzaRepository.findById(id);
        Pietanza foundPietanza = pietanza.orElseThrow(() -> new IllegalArgumentException("Pietanza non trovata con id: " + id));
        return mapToPietanzaResponse(foundPietanza);
    }

    public List<PietanzaResponse> getAllPietanzeResponse() {
        return pietanzaRepository.findAll().stream()
                .map(this::mapToPietanzaResponse)
                .collect(Collectors.toList());
    }

    public PietanzaResponse create(PietanzaRequest pietanzaRequest) {
        Pietanza pietanza = Pietanza.builder()
                .nome(pietanzaRequest.getNome())
                .prezzo(pietanzaRequest.getPrezzo())
                .build();
        Pietanza savedPietanza = pietanzaRepository.saveAndFlush(pietanza);
        return mapToPietanzaResponse(savedPietanza);
    }

    public PietanzaResponse update(Long id, PietanzaRequest pietanzaRequest) {
        Pietanza pietanza = getPietanzaById(id);
        pietanza.setNome(pietanzaRequest.getNome());
        pietanza.setPrezzo(pietanzaRequest.getPrezzo());
        Pietanza updatedPietanza = pietanzaRepository.saveAndFlush(pietanza);
        return mapToPietanzaResponse(updatedPietanza);
    }

    public void deletePietanza(Long id) {
        pietanzaRepository.deleteById(id);
    }
}
