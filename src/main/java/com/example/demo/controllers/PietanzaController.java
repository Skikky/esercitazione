package com.example.demo.controllers;

import com.example.demo.entities.Pietanza;
import com.example.demo.request.PietanzaRequest;
import com.example.demo.services.PietanzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pietanza")
public class PietanzaController {

    @Autowired
    private PietanzaService pietanzaService;

    @GetMapping("/get/{id}")
    public ResponseEntity<Pietanza> getPietanzaById(@PathVariable Long id) {
        Pietanza pietanza = pietanzaService.getPietanzaById(id);
        return ResponseEntity.ok(pietanza);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Pietanza>> getAllPietanze() {
        List<Pietanza> pietanze = pietanzaService.getAllPietanze();
        return ResponseEntity.ok(pietanze);
    }

    @PostMapping("/create")
    public ResponseEntity<Pietanza> createPietanza(@RequestBody PietanzaRequest pietanzaRequest) {
        Pietanza newPietanza = pietanzaService.create(pietanzaRequest);
        return ResponseEntity.ok(newPietanza);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Pietanza> updatePietanza(@PathVariable Long id, @RequestBody PietanzaRequest pietanzaRequest) {
        Pietanza updatedPietanza = pietanzaService.update(id, pietanzaRequest);
        return ResponseEntity.ok(updatedPietanza);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePietanza(@PathVariable Long id) {
        pietanzaService.deletePietanza(id);
        return ResponseEntity.noContent().build();
    }
}
