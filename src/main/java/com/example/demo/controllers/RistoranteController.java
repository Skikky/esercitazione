package com.example.demo.controllers;

import com.example.demo.entities.Ristorante;
import com.example.demo.request.RistoranteRequest;
import com.example.demo.services.RistoranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ristorante")
public class RistoranteController {

    @Autowired
    private RistoranteService ristoranteService;

    @GetMapping("/get/{id}")
    public ResponseEntity<Ristorante> getRistoranteById(@PathVariable Long id) {
        Ristorante ristorante = ristoranteService.getRistoranteById(id);
        return ResponseEntity.ok(ristorante);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Ristorante>> getAllRistoranti() {
        List<Ristorante> ristoranti = ristoranteService.getAllRistoranti();
        return ResponseEntity.ok(ristoranti);
    }

    @PostMapping("/create")
    public ResponseEntity<Ristorante> createRistorante(@RequestBody RistoranteRequest ristoranteRequest) {
        Ristorante newRistorante = ristoranteService.createRistorante(ristoranteRequest);
        return ResponseEntity.ok(newRistorante);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Ristorante> updateRistorante(@PathVariable Long id, @RequestBody RistoranteRequest ristoranteRequest) {
        Ristorante updatedRistorante = ristoranteService.update(id, ristoranteRequest);
        return ResponseEntity.ok(updatedRistorante);
    }

    @PostMapping("/{ristoranteId}/aggiungi_pietanza/")
    public ResponseEntity<Ristorante> aggiungiPietanzaAlMenu(@PathVariable Long ristoranteId, @RequestParam Long pietanzaId) {
        Ristorante updatedRistorante = ristoranteService.aggiungiPietanzaAlMenu(ristoranteId, pietanzaId);
        return ResponseEntity.ok(updatedRistorante);
    }

    @DeleteMapping("/{ristoranteId}/rimuovi_pietanza/")
    public ResponseEntity<Ristorante> rimuoviPietanzaDalMenu(@PathVariable Long ristoranteId, @RequestParam Long pietanzaId) {
        Ristorante updatedRistorante = ristoranteService.rimuoviPietanzaDalMenu(ristoranteId, pietanzaId);
        return ResponseEntity.ok(updatedRistorante);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteRistorante(@PathVariable Long id) {
        ristoranteService.deleteRistorante(id);
        return ResponseEntity.noContent().build();
    }
}