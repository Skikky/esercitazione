package com.example.demo.controllers;


import com.example.demo.entities.Prenotazione;
import com.example.demo.request.PrenotazioneRequest;
import com.example.demo.services.PrenotazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prenotazione")
public class PrenotazioneController {

    @Autowired
    private PrenotazioneService prenotazioneService;

    @GetMapping("/get/{id}")
    public ResponseEntity<Prenotazione> getPrenotazioneById(@PathVariable Long id) {
        Prenotazione prenotazione = prenotazioneService.getPrenotazioneById(id);
        return ResponseEntity.ok(prenotazione);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Prenotazione>> getAllPrenotazioni() {
        List<Prenotazione> prenotazioni = prenotazioneService.getAllPrenotazioni();
        return ResponseEntity.ok(prenotazioni);
    }
/*
    @PutMapping("/update/{id}")
    public ResponseEntity<Prenotazione> updatePrenotazione(@PathVariable Long id, @RequestBody PrenotazioneRequest prenotazioneRequest) {
        Prenotazione updatedPrenotazione = prenotazioneService.updatePrenotazione(id, prenotazioneRequest);
        return ResponseEntity.ok(updatedPrenotazione);
    }
 */

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePrenotazione(@PathVariable Long id) {
        prenotazioneService.deletePrenotazione(id);
        return ResponseEntity.noContent().build();
    }
}