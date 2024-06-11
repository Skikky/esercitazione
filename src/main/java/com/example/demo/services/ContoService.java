package com.example.demo.services;

import com.example.demo.entities.*;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.producer.KafkaJsonProducer;
import com.example.demo.repositories.ContoRepository;
import com.example.demo.repositories.PrenotazioneRepository;
import com.example.demo.repositories.RistoranteRepository;
import com.example.demo.repositories.UtenteRepository;
import com.example.demo.response.ContoResponse;
import com.example.demo.response.KafkaContoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContoService {

    @Autowired
    private ContoRepository contoRepository;
    @Autowired
    private PrenotazioneRepository prenotazioneRepository;
    @Autowired
    private UtenteRepository utenteRepository;
    @Autowired
    private RistoranteRepository ristoranteRepository;
    @Autowired
    private KafkaJsonProducer kafkaJsonProducer;

    public Conto getContoById(Long id) throws EntityNotFoundException {
        return contoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id,"Conto"));
    }
    public ContoResponse getContoResponseById(Long id) throws EntityNotFoundException {
        Conto conto = getContoById(id);
        return mapToContoResponse(conto);
    }

    public List<ContoResponse> getAllConti() {
        return contoRepository.findAll().stream()
                .map(this::mapToContoResponse)
                .collect(Collectors.toList());
    }

    public void createConto(Prenotazione prenotazione) {

        Double totale = prenotazione.getRistorante().getMenu().stream()
                .mapToDouble(Pietanza::getPrezzo)
                .sum();

        Conto conto = Conto.builder()
                .prenotazione(prenotazione)
                .totale(totale)
                .timestamp(LocalDateTime.now())
                .isPagato(false)
                .build();

        Conto savedConto = contoRepository.saveAndFlush(conto);
        mapToContoResponse(savedConto);
    }

    public ContoResponse updateConto(Long id, Double totale) throws EntityNotFoundException {
        Conto conto = getContoById(id);

        conto.setTotale(totale);
        conto.setTimestamp(LocalDateTime.now());

        Conto updatedConto = contoRepository.saveAndFlush(conto);
        return mapToContoResponse(updatedConto);
    }

    @Transactional
    public void chiudiConto(Long contoId) throws EntityNotFoundException {
        Conto conto = getContoById(contoId);
        Prenotazione prenotazione = conto.getPrenotazione();
        Utente utente = prenotazione.getUtente();
        Ristorante ristorante = prenotazione.getRistorante();

        if (conto.getIsPagato()) {
            throw new IllegalStateException("Il conto è già stato pagato.");
        }

        if (utente.getSaldo() < conto.getTotale()) {
            throw new IllegalStateException("Saldo insufficiente, mo devi fa il lava piatti");
        }

        utente.setSaldo(utente.getSaldo() - conto.getTotale());
        utenteRepository.saveAndFlush(utente);

        conto.setIsPagato(true);
        conto.setTimestamp(LocalDateTime.now());

        KafkaContoDTO contoDTO = mapToKafkaContoDTO(conto);
        kafkaJsonProducer.sendMessage(contoDTO);

        ristorante.setPosti(ristorante.getPosti() + prenotazione.getNumeroPosti());
        ristoranteRepository.saveAndFlush(ristorante);

        kafkaJsonProducer.sendMessage(conto);

        conto.setPrenotazione(null);
        contoRepository.saveAndFlush(conto);
        contoRepository.delete(conto);
        contoRepository.flush();
    }

    public void deleteContoByPrenotazione(Prenotazione prenotazione) {
        Conto conto = contoRepository.findByPrenotazione(prenotazione);
        if (conto != null) {
            contoRepository.delete(conto);
        }
    }

    private ContoResponse mapToContoResponse(Conto conto) {
        Long prenotazioneId = null;
        if (conto.getPrenotazione() != null) {
            prenotazioneId = conto.getPrenotazione().getId();
        }

        return ContoResponse.builder()
                .id(conto.getId())
                .prenotazioneId(prenotazioneId)
                .totale(conto.getTotale())
                .timestamp(conto.getTimestamp())
                .isPagato(conto.getIsPagato())
                .build();
    }

    private KafkaContoDTO mapToKafkaContoDTO(Conto conto) {
        return KafkaContoDTO.builder()
                .idConto(conto.getId())
                .prenotazioneId(conto.getPrenotazione() != null ? conto.getPrenotazione().getId() : null)
                .idUtente(conto.getPrenotazione() != null && conto.getPrenotazione().getUtente() != null ? conto.getPrenotazione().getUtente().getId() : null)
                .idRistorante(conto.getPrenotazione() != null && conto.getPrenotazione().getRistorante() != null ? conto.getPrenotazione().getRistorante().getId() : null)
                .totale(conto.getTotale())
                .timestamp(conto.getTimestamp())
                .isPagato(conto.getIsPagato())
                .build();
    }
}

