package com.example.demo.controllers;

import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.response.ContoResponse;
import com.example.demo.services.ContoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conto")
public class ContoController {

    @Autowired
    private ContoService contoService;

    @Secured({"ADMIN", "RISTORATORE", "UTENTE"})
    @GetMapping("/get/{id}")
    public ResponseEntity<ContoResponse> getContoById(@PathVariable Long id) throws EntityNotFoundException {
        ContoResponse conto = contoService.getContoResponseById(id);
        return ResponseEntity.ok(conto);
    }

    @Secured({"ADMIN", "RISTORATORE"})
    @GetMapping("/all")
    public ResponseEntity<List<ContoResponse>> getAllConti() {
        List<ContoResponse> conti = contoService.getAllConti();
        return ResponseEntity.ok(conti);
    }

    @Secured({"ADMIN", "RISTORATORE"})
    @PutMapping("/update/{id}")
    public ResponseEntity<ContoResponse> updateConto(@PathVariable Long id, @RequestBody Double totale) throws EntityNotFoundException {
        ContoResponse updatedConto = contoService.updateConto(id, totale);
        return ResponseEntity.ok(updatedConto);
    }
}
