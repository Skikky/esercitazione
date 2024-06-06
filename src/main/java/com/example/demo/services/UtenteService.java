package com.example.demo.services;

import com.example.demo.entities.*;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.repositories.RistoranteRepository;
import com.example.demo.repositories.UtenteRepository;
import com.example.demo.request.PrenotazioneRequest;
import com.example.demo.request.RegistrationRequest;
import com.example.demo.response.PrenotazioneResponse;
import com.example.demo.response.RistoranteResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UtenteService {

    @Autowired
    private UtenteRepository utenteRepository;
    @Autowired
    private RistoranteRepository ristoranteRepository;
    @Autowired
    private ComuneService comuneService;
    @Autowired
    private PrenotazioneService prenotazioneService;
    @Autowired
    private ContoService contoService;

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

    public Utente getUtenteById(Long id) throws EntityNotFoundException {
        Optional<Utente> utente = utenteRepository.findById(id);
        return utente.orElseThrow(() -> new EntityNotFoundException(id,"Utente :"));
    }

    public List<Utente> getAllUtenti() {
        return utenteRepository.findAll();
    }

    public Utente update(Long id, RegistrationRequest registrationRequest) throws EntityNotFoundException {
        Utente utente = getUtenteById(id);
        utente = Utente.builder()
                .id(utente.getId())
                .nome(registrationRequest.getNome())
                .cognome(registrationRequest.getCognome())
                .email(registrationRequest.getEmail())
                .password(registrationRequest.getPassword())
                .saldo(registrationRequest.getSaldo())
                .build();
        return utenteRepository.saveAndFlush(utente);
    }

    public List<RistoranteResponse> findRistorantiByComune(Long idComune) {
        return ristoranteRepository.findByComuneId(idComune).stream()
                .map(this::mapToRistoranteResponse)
                .collect(Collectors.toList());
    }

    public void deleteUtente(Long id) throws EntityNotFoundException {
        getUtenteById(id);
        utenteRepository.deleteById(id);
    }

    public PrenotazioneResponse prenota(Long id, PrenotazioneRequest prenotazioneRequest) {
        return prenotazioneService.createPrenotazione(id, prenotazioneRequest);
    }

    public void deletePrenotazione(Long userId, Long prenotazioneId) {
        Prenotazione prenotazione = prenotazioneService.getPrenotazioneById(prenotazioneId);

        if (!prenotazione.getUtente().getId().equals(userId)) {
            throw new IllegalArgumentException("L'utente non è autorizzato a eliminare questa prenotazione.");
        }

        prenotazioneService.deletePrenotazione(prenotazioneId);
    }

    public void chiudiConto(Long utenteId, Long contoId) throws EntityNotFoundException {
        Conto conto = contoService.getContoById(contoId);
        getUtenteById(utenteId);

        if (!conto.getPrenotazione().getUtente().getId().equals(utenteId)) {
            throw new IllegalStateException("L'utente non è autorizzato a chiudere questo conto.");
        } else {
            contoService.chiudiConto(contoId);
        }
    }
}