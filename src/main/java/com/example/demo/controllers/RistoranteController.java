package com.example.demo.controllers;

import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.request.RistoranteRequest;
import com.example.demo.response.RistoranteResponse;
import com.example.demo.services.RistoranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ristorante")
public class RistoranteController {

    @Autowired
    private RistoranteService ristoranteService;

    @GetMapping("/get/{id}")
    public ResponseEntity<RistoranteResponse> getRistoranteById(@PathVariable Long id) {
        RistoranteResponse ristorante = ristoranteService.getRistoranteResponseById(id);
        return ResponseEntity.ok(ristorante);
    }

    @GetMapping("/all")
    public ResponseEntity<List<RistoranteResponse>> getAllRistoranti() {
        List<RistoranteResponse> ristoranti = ristoranteService.getAllRistorantiResponse();
        return ResponseEntity.ok(ristoranti);
    }

    @Secured({"ADMIN", "RISTORATORE"})
    @PostMapping("/create")
    public ResponseEntity<RistoranteResponse> createRistorante(@RequestBody RistoranteRequest ristoranteRequest) throws EntityNotFoundException {
        RistoranteResponse newRistorante = ristoranteService.createRistorante(ristoranteRequest);
        return ResponseEntity.ok(newRistorante);
    }

    @Secured({"ADMIN", "RISTORATORE"})
    @PutMapping("/update/{id}")
    public ResponseEntity<RistoranteResponse> updateRistorante(@PathVariable Long id, @RequestBody RistoranteRequest ristoranteRequest) throws EntityNotFoundException {
        RistoranteResponse updatedRistorante = ristoranteService.update(id, ristoranteRequest);
        return ResponseEntity.ok(updatedRistorante);
    }

    @Secured({"RISTORATORE"})
    @PostMapping("/{ristoranteId}/aggiungi_pietanza/")
    public ResponseEntity<RistoranteResponse> aggiungiPietanzaAlMenu(@PathVariable Long ristoranteId, @RequestParam Long pietanzaId) {
        RistoranteResponse updatedRistorante = ristoranteService.aggiungiPietanzaAlMenu(ristoranteId, pietanzaId);
        return ResponseEntity.ok(updatedRistorante);
    }

    @Secured({"RISTORATORE"})
    @DeleteMapping("/{ristoranteId}/rimuovi_pietanza/")
    public ResponseEntity<RistoranteResponse> rimuoviPietanzaDalMenu(@PathVariable Long ristoranteId, @RequestParam Long pietanzaId) {
        RistoranteResponse updatedRistorante = ristoranteService.rimuoviPietanzaDalMenu(ristoranteId, pietanzaId);
        return ResponseEntity.ok(updatedRistorante);
    }

    @Secured({"ADMIN", "RISTORATORE"})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteRistorante(@PathVariable Long id) {
        ristoranteService.deleteRistorante(id);
        return ResponseEntity.noContent().build();
    }
}