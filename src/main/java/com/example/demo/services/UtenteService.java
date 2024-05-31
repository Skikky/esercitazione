package com.example.demo.services;

import com.example.demo.entities.Prenotazione;
import com.example.demo.entities.Ristorante;
import com.example.demo.entities.Utente;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.repositories.RistoranteRepository;
import com.example.demo.repositories.UtenteRepository;
import com.example.demo.request.PrenotazioneRequest;
import com.example.demo.response.PrenotazioneResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Utente getUtenteById(Long id) throws EntityNotFoundException {
        Optional<Utente> utente = utenteRepository.findById(id);
        return utente.orElseThrow(() -> new EntityNotFoundException(id,"Utente :"));
    }

    public List<Utente> getAllUtenti() {
        return utenteRepository.findAll();
    }

    public Utente update(Long id, Utente newUtente) throws EntityNotFoundException {
        Utente utente = getUtenteById(id);
        utente = Utente.builder()
                .id(utente.getId())
                .nome(newUtente.getNome())
                .cognome(newUtente.getCognome())
                .email(newUtente.getEmail())
                .password(newUtente.getPassword())
                .build();
        return utenteRepository.saveAndFlush(utente);
    }

    public List<Ristorante> findRistorantiByComune(Long idComune) {
        return ristoranteRepository.findByComune(idComune);
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

    public void chiudiConto(Long userId, Long prenotazioneId) {
        Prenotazione prenotazione = prenotazioneService.getPrenotazioneById(prenotazioneId);
        if (!prenotazione.getUtente().getId().equals(userId)) {
            throw new IllegalArgumentException("L'utente non è autorizzato a chiudere il conto di questa prenotazione.");
        }
        prenotazioneService.chiudiConto(prenotazioneId);
    }

}