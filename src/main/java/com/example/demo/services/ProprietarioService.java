package com.example.demo.services;

import com.example.demo.entities.Proprietario;
import com.example.demo.entities.Ristorante;
import com.example.demo.enums.Role;
import com.example.demo.repositories.ProprietarioRepository;
import com.example.demo.repositories.RistoranteRepository;
import com.example.demo.request.RegistrationRequest;
import com.example.demo.response.ProprietarioResponse;
import com.example.demo.security.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProprietarioService {

    @Autowired
    private ProprietarioRepository proprietarioRepository;
    @Autowired
    private RistoranteRepository ristoranteRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    public Proprietario getProprietarioById(Long id) {
        Optional<Proprietario> proprietario = proprietarioRepository.findById(id);
        return proprietario.orElseThrow(() -> new IllegalArgumentException("Proprietario non trovato con id: " + id));
    }

    public ProprietarioResponse getProprietarioResponseById(Long id) {
        Optional<Proprietario> proprietario = proprietarioRepository.findById(id);
        Proprietario foundProprietario = proprietario.orElseThrow(() -> new IllegalArgumentException("Proprietario non trovato con id: " + id));
        return mapToProprietarioResponse(foundProprietario);
    }

    public List<ProprietarioResponse> getAllProprietariResponse() {
        return proprietarioRepository.findAll().stream()
                .map(this::mapToProprietarioResponse)
                .collect(Collectors.toList());
    }

    public ProprietarioResponse create(RegistrationRequest registrationRequest) {
        Proprietario proprietario = new Proprietario();
        proprietario.setNome(registrationRequest.getNome());
        proprietario.setCognome(registrationRequest.getCognome());
        proprietario.setEmail(registrationRequest.getEmail());
        proprietario.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        proprietario.setRole(Role.RISTORATORE);
        var jwtToken = jwtService.generateToken(proprietario);
        proprietario.setRegistrationToken(jwtToken);
        Proprietario savedProprietario = proprietarioRepository.saveAndFlush(proprietario);
        return mapToProprietarioResponse(savedProprietario);
    }

    public ProprietarioResponse update(Long id, RegistrationRequest registrationRequest) {
        Proprietario proprietario = getProprietarioById(id);
        proprietario.setNome(registrationRequest.getNome());
        proprietario.setCognome(registrationRequest.getCognome());
        proprietario.setEmail(registrationRequest.getEmail());
        proprietario.setPassword(registrationRequest.getPassword());
        Proprietario savedProprietario = proprietarioRepository.saveAndFlush(proprietario);
        return mapToProprietarioResponse(savedProprietario);
    }

    @Transactional
    public void addRistoranteToProprietario(Proprietario proprietario, Ristorante ristorante) {
        proprietario.getRistoranti().add(ristorante);
        proprietarioRepository.saveAndFlush(proprietario);
    }

    public void deleteProprietario(Long id) {
        proprietarioRepository.deleteById(id);
    }

    private ProprietarioResponse mapToProprietarioResponse(Proprietario proprietario) {
        return ProprietarioResponse.builder()
                .nome(proprietario.getNome())
                .cognome(proprietario.getCognome())
                .email(proprietario.getEmail())
                .password(proprietario.getPassword())
                .idRistoranti(proprietario.getRistoranti().stream()
                        .map(Ristorante::getId)
                        .collect(Collectors.toList()))
                .build();
    }
}
