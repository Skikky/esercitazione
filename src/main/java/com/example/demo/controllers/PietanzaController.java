package com.example.demo.controllers;

import com.example.demo.entities.Pietanza;
import com.example.demo.request.PietanzaRequest;
import com.example.demo.response.PietanzaResponse;
import com.example.demo.services.PietanzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pietanza")
public class PietanzaController {

    @Autowired
    private PietanzaService pietanzaService;

    @Secured({"ADMIN", "RISTORATORE", "UTENTE"})
    @GetMapping("/get/{id}")
    public ResponseEntity<PietanzaResponse> getPietanzaById(@PathVariable Long id) {
        PietanzaResponse pietanza = pietanzaService.getPietanzaResponseById(id);
        return ResponseEntity.ok(pietanza);
    }

    @Secured({"ADMIN", "RISTORATORE", "UTENTE"})
    @GetMapping("/all")
    public ResponseEntity<List<PietanzaResponse>> getAllPietanze() {
        List<PietanzaResponse> pietanze = pietanzaService.getAllPietanzeResponse();
        return ResponseEntity.ok(pietanze);
    }

    @Secured({"ADMIN", "RISTORATORE"})
    @PostMapping("/create")
    public ResponseEntity<PietanzaResponse> createPietanza(@RequestBody PietanzaRequest pietanzaRequest) {
        PietanzaResponse newPietanza = pietanzaService.create(pietanzaRequest);
        return ResponseEntity.ok(newPietanza);
    }

    @Secured({"ADMIN", "RISTORATORE"})
    @PutMapping("/update/{id}")
    public ResponseEntity<PietanzaResponse> updatePietanza(@PathVariable Long id, @RequestBody PietanzaRequest pietanzaRequest) {
        PietanzaResponse updatedPietanza = pietanzaService.update(id, pietanzaRequest);
        return ResponseEntity.ok(updatedPietanza);
    }

    @Secured({"ADMIN", "RISTORATORE"})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePietanza(@PathVariable Long id) {
        pietanzaService.deletePietanza(id);
        return ResponseEntity.noContent().build();
    }
}
