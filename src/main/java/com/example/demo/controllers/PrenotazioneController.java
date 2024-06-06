package com.example.demo.controllers;

import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.response.PrenotazioneResponse;
import com.example.demo.services.PrenotazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prenotazione")
public class PrenotazioneController {

    @Autowired
    private PrenotazioneService prenotazioneService;

    @Secured({"ADMIN", "RISTORATORE", "UTENTE"})
    @GetMapping("/get/{id}")
    public ResponseEntity<PrenotazioneResponse> getPrenotazioneById(@PathVariable Long id) throws EntityNotFoundException {
        PrenotazioneResponse prenotazione = prenotazioneService.getPrenotazioneResponseById(id);
        return ResponseEntity.ok(prenotazione);
    }

    @Secured({"ADMIN", "RISTORATORE"})
    @GetMapping("/all")
    public ResponseEntity<List<PrenotazioneResponse>> getAllPrenotazioni() {
        List<PrenotazioneResponse> prenotazioni = prenotazioneService.getAllPrenotazioniResponse();
        return ResponseEntity.ok(prenotazioni);
    }

    @Secured({"ADMIN", "RISTORATORE", "UTENTE"})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePrenotazione(@PathVariable Long id) throws EntityNotFoundException {
        prenotazioneService.deletePrenotazione(id);
        return ResponseEntity.noContent().build();
    }
}