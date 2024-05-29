package com.example.demo.services;

import com.example.demo.entities.Proprietario;
import com.example.demo.entities.Ristorante;
import com.example.demo.repositories.ProprietarioRepository;
import com.example.demo.repositories.RistoranteRepository;
import com.example.demo.request.ProprietarioRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProprietarioService {

    @Autowired
    private ProprietarioRepository proprietarioRepository;
    @Autowired
    private RistoranteRepository ristoranteRepository;

    public Proprietario getProprietarioById(Long id) {
        Optional<Proprietario> proprietario = proprietarioRepository.findById(id);
        return proprietario.orElseThrow(() -> new IllegalArgumentException("Proprietario non trovato con id: " + id));
    }

    public List<Proprietario> getAllProprietari() {
        return proprietarioRepository.findAll();
    }

    public Proprietario create(ProprietarioRequest proprietarioRequest) {
        List<Ristorante> ristoranti = ristoranteRepository.findAllById(proprietarioRequest.getIdRistoranti());
        Proprietario proprietario = new Proprietario();
        proprietario.setNome(proprietarioRequest.getNome());
        proprietario.setCognome(proprietarioRequest.getCognome());
        proprietario.setEmail(proprietarioRequest.getEmail());
        proprietario.setPassword(proprietarioRequest.getPassword());
        proprietario.setRistoranti(ristoranti);
        return proprietarioRepository.saveAndFlush(proprietario);
    }

    public Proprietario update(Long id, ProprietarioRequest proprietarioRequest) {
        Proprietario proprietario = getProprietarioById(id);
        List<Ristorante> ristoranti = ristoranteRepository.findAllById(proprietarioRequest.getIdRistoranti());
        proprietario.setNome(proprietarioRequest.getNome());
        proprietario.setCognome(proprietarioRequest.getCognome());
        proprietario.setEmail(proprietarioRequest.getEmail());
        proprietario.setPassword(proprietarioRequest.getPassword());
        proprietario.setRistoranti(ristoranti);
        return proprietarioRepository.saveAndFlush(proprietario);
    }

    public void deleteProprietario(Long id) {
        proprietarioRepository.deleteById(id);
    }
}
