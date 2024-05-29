package com.example.demo.controllers;

import com.example.demo.entities.Prenotazione;
import com.example.demo.entities.Ristorante;
import com.example.demo.entities.Utente;
import com.example.demo.request.PrenotazioneRequest;
import com.example.demo.response.PrenotazioneResponse;
import com.example.demo.services.RistoranteService;
import com.example.demo.services.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/utente")
public class UtenteController {

    @Autowired
    private UtenteService utenteService;

    @GetMapping("/get/{id}")
    public ResponseEntity<Utente> getUtenteById(@PathVariable Long id) {
        Utente utente = utenteService.getUtenteById(id);
        return ResponseEntity.ok(utente);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Utente>> getAllUtenti() {
        List<Utente> utenti = utenteService.getAllUtenti();
        return ResponseEntity.ok(utenti);
    }

    @PostMapping("/create")
    public ResponseEntity<Utente> createUtente(@RequestBody Utente utente) {
        Utente newUtente = utenteService.create(utente);
        return ResponseEntity.ok(newUtente);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Utente> updateUtente(@PathVariable Long id, @RequestBody Utente utenteDetails) {
        Utente updatedUtente = utenteService.update(id, utenteDetails);
        return ResponseEntity.ok(updatedUtente);
    }

    @GetMapping("/cerca_ristoranti/{idComune}")
    public ResponseEntity<List<Ristorante>> findRistorantiByComune(@PathVariable Long idComune) {
        List<Ristorante> ristoranti = utenteService.findRistorantiByComune(idComune);
        return ResponseEntity.ok(ristoranti);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUtente(@PathVariable Long id) {
        utenteService.deleteUtente(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/prenota")
    public ResponseEntity<PrenotazioneResponse> prenota(@PathVariable Long id, @RequestBody PrenotazioneRequest prenotazioneRequest) {
        PrenotazioneResponse newPrenotazione = utenteService.prenota(id, prenotazioneRequest);
        return ResponseEntity.ok(newPrenotazione);
    }

    @DeleteMapping("/{userId}/prenotazioni/{prenotazioneId}")
    public ResponseEntity<Void> deletePrenotazione(@PathVariable Long userId, @PathVariable Long prenotazioneId) {
        utenteService.deletePrenotazione(userId, prenotazioneId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/chiudi_conto/{prenotazioneId}")
    public ResponseEntity<PrenotazioneResponse> chiudiConto(@PathVariable Long userId, @PathVariable Long prenotazioneId) {
        PrenotazioneResponse prenotazioneResponse = utenteService.chiudiConto(userId, prenotazioneId);
        return ResponseEntity.ok(prenotazioneResponse);
    }

}