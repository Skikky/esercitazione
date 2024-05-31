package com.example.demo.controllers;

import com.example.demo.entities.Proprietario;
import com.example.demo.request.ProprietarioRequest;
import com.example.demo.services.ProprietarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/proprietario")
public class ProprietarioController {

    @Autowired
    private ProprietarioService proprietarioService;

    @GetMapping("/get/{id}")
    public ResponseEntity<Proprietario> getProprietarioById(@PathVariable Long id) {
        Proprietario proprietario = proprietarioService.getProprietarioById(id);
        return ResponseEntity.ok(proprietario);
    }

    @Secured({"ADMIN", "RISTORATORE"})
    @GetMapping("/all")
    public ResponseEntity<List<Proprietario>> getAllProprietari() {
        List<Proprietario> proprietari = proprietarioService.getAllProprietari();
        return ResponseEntity.ok(proprietari);
    }

    @Secured({"ADMIN", "RISTORATORE"})
    @PostMapping("/create")
    public ResponseEntity<Proprietario> createProprietario(@RequestBody ProprietarioRequest proprietarioRequest) {
        Proprietario newProprietario = proprietarioService.create(proprietarioRequest);
        return ResponseEntity.ok(newProprietario);
    }

    @Secured({"ADMIN", "RISTORATORE"})
    @PutMapping("/update/{id}")
    public ResponseEntity<Proprietario> updateProprietario(@PathVariable Long id, @RequestBody ProprietarioRequest proprietarioRequest) {
        Proprietario updatedProprietario = proprietarioService.update(id, proprietarioRequest);
        return ResponseEntity.ok(updatedProprietario);
    }

    @Secured({"ADMIN", "RISTORATORE"})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProprietario(@PathVariable Long id) {
        proprietarioService.deleteProprietario(id);
        return ResponseEntity.noContent().build();
    }
}