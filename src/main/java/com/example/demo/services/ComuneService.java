package com.example.demo.services;

import com.example.demo.entities.Comune;
import com.example.demo.repositories.ComuneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComuneService {

    @Autowired
    private ComuneRepository comuneRepository;

    public Comune getComuneById(Long id) {
        Optional<Comune> comune = comuneRepository.findById(id);
        return comune.orElseThrow(() -> new IllegalArgumentException("Comune non trovato con id: " + id));
    }

    public List<Comune> getAllComuni() {
        return comuneRepository.findAll();
    }

    public Comune create(Comune comune) {
        return comuneRepository.saveAndFlush(comune);
    }

    public Comune update(Long id, Comune newComune) {
        Comune comune = getComuneById(id);
        comune = Comune.builder()
                .id(comune.getId())
                .nome(newComune.getNome())
                .regione(newComune.getRegione())
                .build();
        return comuneRepository.saveAndFlush(comune);
    }

    public void deleteComune(Long id) {
        comuneRepository.deleteById(id);
    }
}