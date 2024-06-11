package com.example.demo.controllers;

import com.example.demo.entities.Utente;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.request.PrenotazioneRequest;
import com.example.demo.request.RegistrationRequest;
import com.example.demo.response.PrenotazioneResponse;
import com.example.demo.response.RistoranteResponse;
import com.example.demo.services.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/utente")
public class UtenteController {

    @Autowired
    private UtenteService utenteService;

    @Secured({"ADMIN", "RISTORATORE", "UTENTE"})
    @GetMapping("/get/{id}")
    public ResponseEntity<Utente> getUtenteById(@PathVariable Long id) throws EntityNotFoundException {
        Utente utente = utenteService.getUtenteById(id);
        return ResponseEntity.ok(utente);
    }

    @Secured("ADMIN")
    @GetMapping("/all")
    public ResponseEntity<List<Utente>> getAllUtenti() {
        List<Utente> utenti = utenteService.getAllUtenti();
        return ResponseEntity.ok(utenti);
    }

    @Secured({"ADMIN", "RISTORATORE", "UTENTE"})
    @PutMapping("/update/{id}")
    public ResponseEntity<Utente> updateUtente(@PathVariable Long id, @RequestBody RegistrationRequest registrationRequest) throws EntityNotFoundException {
        Utente updatedUtente = utenteService.update(id, registrationRequest);
        return ResponseEntity.ok(updatedUtente);
    }

    @GetMapping("/cerca_ristoranti/{idComune}")
    public ResponseEntity<List<RistoranteResponse>> findRistorantiByComune(@PathVariable Long idComune) {
        List<RistoranteResponse> ristoranti = utenteService.findRistorantiByComune(idComune);
        return ResponseEntity.ok(ristoranti);
    }

    @Secured({"ADMIN", "RISTORATORE", "UTENTE"})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUtente(@PathVariable Long id) throws EntityNotFoundException {
        utenteService.deleteUtente(id);
        return ResponseEntity.noContent().build();
    }

    @Secured({"ADMIN", "RISTORATORE", "UTENTE"})
    @PostMapping("/{id}/prenota")
    public ResponseEntity<PrenotazioneResponse> prenota(@PathVariable Long id, @RequestBody PrenotazioneRequest prenotazioneRequest) throws EntityNotFoundException {
        PrenotazioneResponse newPrenotazione = utenteService.prenota(id, prenotazioneRequest);
        return ResponseEntity.ok(newPrenotazione);
    }

    @Secured({"ADMIN", "RISTORATORE", "UTENTE"})
    @DeleteMapping("/{userId}/prenotazioni/{prenotazioneId}")
    public ResponseEntity<Void> deletePrenotazione(@PathVariable Long userId, @PathVariable Long prenotazioneId) throws EntityNotFoundException {
        utenteService.deletePrenotazione(userId, prenotazioneId);
        return ResponseEntity.noContent().build();
    }

    @Secured({"ADMIN", "RISTORATORE", "UTENTE"})
    @PostMapping("/{userId}/chiudi_conto/{contoId}")
    public ResponseEntity<Void> chiudiConto(@PathVariable Long userId, @PathVariable Long contoId) throws EntityNotFoundException {
        utenteService.chiudiConto(userId, contoId);
        return ResponseEntity.noContent().build();
    }

}