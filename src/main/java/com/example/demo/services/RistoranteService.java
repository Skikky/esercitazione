package com.example.demo.services;

import com.example.demo.entities.Comune;
import com.example.demo.entities.Pietanza;
import com.example.demo.entities.Proprietario;
import com.example.demo.entities.Ristorante;
import com.example.demo.repositories.PietanzaRepository;
import com.example.demo.repositories.RistoranteRepository;
import com.example.demo.request.RistoranteRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RistoranteService {
    @Autowired
    private RistoranteRepository ristoranteRepository;
    @Autowired
    private PietanzaRepository pietanzaRepository;
    @Autowired
    private ComuneService comuneService;
    @Autowired
    private ProprietarioService proprietarioService;

    public Ristorante createRistorante(RistoranteRequest ristoranteRequest) {
        Comune comune = comuneService.getComuneById(ristoranteRequest.getIdComune());
        Proprietario proprietario = proprietarioService.getProprietarioById(ristoranteRequest.getIdProprietario());
        Set<Pietanza> pietanze = new HashSet<>(pietanzaRepository.findAllById(ristoranteRequest.getIdPietanze()));

        Ristorante ristorante = Ristorante.builder()
                .nome(ristoranteRequest.getNome())
                .indirizzo(ristoranteRequest.getIndirizzo())
                .comune(comune)
                .proprietario(proprietario)
                .posti(ristoranteRequest.getPosti())
                .apertura(ristoranteRequest.getApertura())
                .chiusura(ristoranteRequest.getChiusura())
                .menu(pietanze)
                .build();

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

    public Ristorante update(Long id, RistoranteRequest ristoranteRequest) {
        Ristorante ristorante = getRistoranteById(id);
        Comune comune = comuneService.getComuneById(ristoranteRequest.getIdComune());
        Proprietario proprietario = proprietarioService.getProprietarioById(ristoranteRequest.getIdProprietario());
        Set<Pietanza> pietanze = new HashSet<>(pietanzaRepository.findAllById(ristoranteRequest.getIdPietanze()));
        ristorante = Ristorante.builder()
                .id(id)
                .nome(ristoranteRequest.getNome())
                .indirizzo(ristoranteRequest.getIndirizzo())
                .comune(comune)
                .proprietario(proprietario)
                .posti(ristoranteRequest.getPosti())
                .apertura(ristoranteRequest.getApertura())
                .chiusura(ristoranteRequest.getChiusura())
                .menu(pietanze)
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
