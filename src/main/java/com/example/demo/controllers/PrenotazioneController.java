package com.example.demo.controllers;


import com.example.demo.entities.Prenotazione;
import com.example.demo.request.PrenotazioneRequest;
import com.example.demo.response.PrenotazioneResponse;
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
    public ResponseEntity<PrenotazioneResponse> getPrenotazioneById(@PathVariable Long id) {
        PrenotazioneResponse prenotazione = prenotazioneService.getPrenotazioneResponseById(id);
        return ResponseEntity.ok(prenotazione);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PrenotazioneResponse>> getAllPrenotazioni() {
        List<PrenotazioneResponse> prenotazioni = prenotazioneService.getAllPrenotazioniResponse();
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