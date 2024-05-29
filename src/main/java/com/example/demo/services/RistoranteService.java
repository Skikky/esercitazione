package com.example.demo.services;

import com.example.demo.entities.Comune;
import com.example.demo.entities.Pietanza;
import com.example.demo.entities.Ristorante;
import com.example.demo.repositories.PietanzaRepository;
import com.example.demo.repositories.RistoranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RistoranteService {
    @Autowired
    private RistoranteRepository ristoranteRepository;
    @Autowired
    private PietanzaRepository pietanzaRepository;

    public Ristorante createRistorante(Ristorante ristorante) {
        return ristoranteRepository.saveAndFlush(ristorante);
    }

    public Ristorante getRistoranteById(Long id) {
        Optional<Ristorante> optionalRistorante = ristoranteRepository.findById(id);
        return optionalRistorante.orElseThrow(() -> new IllegalArgumentException("ristorante non trovato con id: "+id));
    }



    public List<Ristorante> getAllRistoranti() {
        return ristoranteRepository.findAll();
    }

    public void deleteRistorante(Long id) {
        ristoranteRepository.deleteById(id);
    }

    public Ristorante update(Long id, Ristorante newRistorante) {
        Ristorante ristorante = getRistoranteById(id);
        ristorante = Ristorante.builder()
                .id(ristorante.getId())
                .nome(newRistorante.getNome())
                .indirizzo(newRistorante.getIndirizzo())
                .comune(newRistorante.getComune())
                .proprietario(newRistorante.getProprietario())
                .posti(newRistorante.getPosti())
                .apertura(newRistorante.getApertura())
                .chiusura(newRistorante.getChiusura())
                .menu(newRistorante.getMenu())
                .build();
        return ristoranteRepository.saveAndFlush(ristorante);
    }

    public Ristorante aggiungiPietanzaAlMenu(Long ristoranteId, Long pietanzaId) {
        Ristorante ristorante = getRistoranteById(ristoranteId);
        Pietanza pietanza = pietanzaRepository.findById(pietanzaId)
                .orElseThrow(() -> new IllegalArgumentException("Pietanza non trovata con id: " + pietanzaId));

        if (ristorante.getMenu().contains(pietanza)) {
            throw new IllegalArgumentException("La pietanza è già presente nel menu del ristorante.");
        }

        ristorante.getMenu().add(pietanza);
        pietanza.getRistoranti().add(ristorante);
        pietanzaRepository.saveAndFlush(pietanza);
        return ristoranteRepository.saveAndFlush(ristorante);
    }

    public Ristorante rimuoviPietanzaDalMenu(Long ristoranteId, Long pietanzaId) {
        Ristorante ristorante = getRistoranteById(ristoranteId);
        Pietanza pietanza = pietanzaRepository.findById(pietanzaId)
                .orElseThrow(() -> new IllegalArgumentException("Pietanza non trovata con id: " + pietanzaId));

        if (!ristorante.getMenu().contains(pietanza)) {
            throw new IllegalArgumentException("La pietanza non è presente nel menu del ristorante.");
        }

        ristorante.getMenu().remove(pietanza);
        pietanza.getRistoranti().remove(ristorante);
        pietanzaRepository.saveAndFlush(pietanza);
        return ristoranteRepository.saveAndFlush(ristorante);
    }
}
