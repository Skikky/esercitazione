package com.example.demo.services;

import com.example.demo.entities.Comune;
import com.example.demo.entities.Pietanza;
import com.example.demo.entities.Proprietario;
import com.example.demo.entities.Ristorante;
import com.example.demo.repositories.PietanzaRepository;
import com.example.demo.repositories.RistoranteRepository;
import com.example.demo.request.RistoranteRequest;
import com.example.demo.response.RistoranteResponse;
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

    public Ristorante getRistoranteById(Long id) {
        Optional<Ristorante> optionalRistorante = ristoranteRepository.findById(id);
        return optionalRistorante.orElseThrow(() -> new IllegalArgumentException("ristorante non trovato con id: "+id));
    }

    public void deleteRistorante(Long id) {
        ristoranteRepository.deleteById(id);
    }

    private RistoranteResponse mapToRistoranteResponse(Ristorante ristorante) {
        return RistoranteResponse.builder()
                .idRistorante(ristorante.getId())
                .nome(ristorante.getNome())
                .indirizzo(ristorante.getIndirizzo())
                .idComune(ristorante.getComune().getId())
                .idProprietario(ristorante.getProprietario().getId())
                .posti(ristorante.getPosti())
                .apertura(ristorante.getApertura())
                .chiusura(ristorante.getChiusura())
                .idPietanze(ristorante.getMenu().stream().map(Pietanza::getId).collect(Collectors.toSet()))
                .build();
    }

    public RistoranteResponse createRistorante(RistoranteRequest ristoranteRequest) {
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

        Ristorante savedRistorante = ristoranteRepository.saveAndFlush(ristorante);
        return mapToRistoranteResponse(savedRistorante);
    }

    public RistoranteResponse getRistoranteResponseById(Long id) {
        Optional<Ristorante> optionalRistorante = ristoranteRepository.findById(id);
        Ristorante ristorante = optionalRistorante.orElseThrow(() -> new IllegalArgumentException("ristorante non trovato con id: " + id));
        return mapToRistoranteResponse(ristorante);
    }

    public List<RistoranteResponse> getAllRistorantiResponse() {
        return ristoranteRepository.findAll().stream()
                .map(this::mapToRistoranteResponse)
                .collect(Collectors.toList());
    }

    public RistoranteResponse update(Long id, RistoranteRequest ristoranteRequest) {
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

        Ristorante updatedRistorante = ristoranteRepository.saveAndFlush(ristorante);
        return mapToRistoranteResponse(updatedRistorante);
    }

    public RistoranteResponse aggiungiPietanzaAlMenu(Long ristoranteId, Long pietanzaId) {
        Ristorante ristorante = getRistoranteById(ristoranteId);
        Pietanza pietanza = pietanzaRepository.findById(pietanzaId)
                .orElseThrow(() -> new IllegalArgumentException("Pietanza non trovata con id: " + pietanzaId));

        if (ristorante.getMenu().contains(pietanza)) {
            throw new IllegalArgumentException("La pietanza è già presente nel menu del ristorante.");
        }

        ristorante.getMenu().add(pietanza);
        pietanza.getRistoranti().add(ristorante);
        pietanzaRepository.saveAndFlush(pietanza);
        Ristorante updatedRistorante = ristoranteRepository.saveAndFlush(ristorante);
        return mapToRistoranteResponse(updatedRistorante);
    }

    public RistoranteResponse rimuoviPietanzaDalMenu(Long ristoranteId, Long pietanzaId) {
        Ristorante ristorante = getRistoranteById(ristoranteId);
        Pietanza pietanza = pietanzaRepository.findById(pietanzaId)
                .orElseThrow(() -> new IllegalArgumentException("Pietanza non trovata con id: " + pietanzaId));

        if (!ristorante.getMenu().contains(pietanza)) {
            throw new IllegalArgumentException("La pietanza non è presente nel menu del ristorante.");
        }

        ristorante.getMenu().remove(pietanza);
        pietanza.getRistoranti().remove(ristorante);
        pietanzaRepository.saveAndFlush(pietanza);
        Ristorante updatedRistorante = ristoranteRepository.saveAndFlush(ristorante);
        return mapToRistoranteResponse(updatedRistorante);
    }
}
