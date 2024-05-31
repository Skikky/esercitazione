package com.example.demo.controllers;

import com.example.demo.entities.Comune;
import com.example.demo.services.ComuneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Secured({"ADMIN"})
@RequestMapping("/comune")
public class ComuneController {

    @Autowired
    private ComuneService comuneService;

    @Secured({"ADMIN", "RISTORATORE", "UTENTE"})
    @GetMapping("/get/{id}")
    public ResponseEntity<Comune> getComuneById(@PathVariable Long id) {
        Comune comune = comuneService.getComuneById(id);
        return ResponseEntity.ok(comune);
    }

    @Secured({"ADMIN", "RISTORATORE", "UTENTE"})
    @GetMapping("/all")
    public ResponseEntity<List<Comune>> getAllComuni() {
        List<Comune> comuni = comuneService.getAllComuni();
        return ResponseEntity.ok(comuni);
    }

    @Secured({"ADMIN"})
    @PostMapping("/create")
    public ResponseEntity<Comune> createComune(@RequestBody Comune comune) {
        Comune newComune = comuneService.create(comune);
        return ResponseEntity.ok(newComune);
    }

    @Secured({"ADMIN"})
    @PutMapping("/update/{id}")
    public ResponseEntity<Comune> updateComune(@PathVariable Long id, @RequestBody Comune comuneDetails) {
        Comune updatedComune = comuneService.update(id, comuneDetails);
        return ResponseEntity.ok(updatedComune);
    }

    @Secured({"ADMIN"})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteComune(@PathVariable Long id) {
        comuneService.deleteComune(id);
        return ResponseEntity.noContent().build();
    }
}