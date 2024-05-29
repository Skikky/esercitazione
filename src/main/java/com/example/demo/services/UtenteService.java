package com.example.demo.services;

import com.example.demo.entities.Comune;
import com.example.demo.entities.Ristorante;
import com.example.demo.entities.Utente;
import com.example.demo.repositories.RistoranteRepository;
import com.example.demo.repositories.UtenteRepository;
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

    public Utente getUtenteById(Long id) {
        Optional<Utente> utente = utenteRepository.findById(id);
        return utente.orElseThrow(() -> new IllegalArgumentException("Utente non trovato con id: " + id));
    }

    public List<Utente> getAllUtenti() {
        return utenteRepository.findAll();
    }

    public Utente create(Utente utente) {
        return utenteRepository.saveAndFlush(utente);
    }

    public Utente update(Long id, Utente newUtente) {
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

    public void deleteUtente(Long id) {
        utenteRepository.deleteById(id);
    }
}