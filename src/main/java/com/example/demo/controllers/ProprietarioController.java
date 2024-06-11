package com.example.demo.controllers;

import com.example.demo.entities.Proprietario;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.request.RegistrationRequest;
import com.example.demo.response.ProprietarioResponse;
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
    public ResponseEntity<ProprietarioResponse> getProprietarioById(@PathVariable Long id) throws EntityNotFoundException {
        ProprietarioResponse proprietario = proprietarioService.getProprietarioResponseById(id);
        return ResponseEntity.ok(proprietario);
    }

    @Secured({"ADMIN", "RISTORATORE"})
    @GetMapping("/all")
    public ResponseEntity<List<ProprietarioResponse>> getAllProprietari() {
        List<ProprietarioResponse> proprietari = proprietarioService.getAllProprietariResponse();
        return ResponseEntity.ok(proprietari);
    }

    @Secured({"ADMIN", "RISTORATORE"})
    @PostMapping("/create")
    public ResponseEntity<ProprietarioResponse> createProprietario(@RequestBody RegistrationRequest registrationRequest) {
        ProprietarioResponse newProprietario = proprietarioService.create(registrationRequest);
        return ResponseEntity.ok(newProprietario);
    }

    @Secured({"ADMIN", "RISTORATORE"})
    @PutMapping("/update/{id}")
    public ResponseEntity<ProprietarioResponse> updateProprietario(@PathVariable Long id, @RequestBody RegistrationRequest registrationRequest) throws EntityNotFoundException {
        ProprietarioResponse updatedProprietario = proprietarioService.update(id, registrationRequest);
        return ResponseEntity.ok(updatedProprietario);
    }

    @Secured({"ADMIN", "RISTORATORE"})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProprietario(@PathVariable Long id) throws EntityNotFoundException {
        proprietarioService.deleteProprietario(id);
        return ResponseEntity.noContent().build();
    }
}